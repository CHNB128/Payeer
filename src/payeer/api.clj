(ns payeer.api
  (:require
   [org.httpkit.client :as http]
   [clojure.data.json :as json]))

; Base url
(defonce ^:private url "https://payeer.com/ajax/api/api.php")

(def ^:private default-options
  {:form-params {}})

(def ^:private creditionals (atom {}))

(defn- auth [{:keys [account apiId apiPass] :as creditionals}]
  (let [options (-> default-options
                    (assoc :form-params creditionals))
        {:keys [body status error]} @(http/post url options)
        data (json/read-str body :key-fn keyword)]
    (when-not (= "0" (:auth_error data))
      (throw (Exception. (-> data :errors json/write-str))))))

(defn set-creditionals! [{:keys [account apiId apiPass] :as data}]
  (auth data)
  (reset! creditionals data))

(defn- request [url options]
  (when (= {}  @creditionals)
    (throw (Exception. "Creditionals don't set. use `set-creditionals!` first")))
  (let [options (assoc default-options :form-params
                       (merge (:form-params options) @creditionals))
        {:keys [body status error]} @(http/post url options)
        data (json/read-str body :key-fn keyword)]
    ; (println "r" body status error options data)
    (when-not (= [] (:errors data))
      (throw (Exception. (-> data :errors json/write-str))))
    (identity data)))

(defn get-balance []
  (let [options (assoc-in default-options [:form-params :action] "balance")
        data (request url options)]
    (:balance data)))

(defn get-payment-systems []
  (let [options (assoc-in default-options [:form-params :action] "getPaySystems")
        data (request url options)]
    (:list data)))

(defn get-history []
  (let [options (assoc-in default-options [:form-params :action] "history")
        data (request url options)]
    (:history data)))

(defmulti get-exchange-rate
  (fn [type] (identity type)))

(defmethod get-exchange-rate :input [_]
  (let [options (-> default-options
                    (assoc :form-params
                           (merge (:form-params default-options)
                                  {:output "N"}))
                    (assoc-in [:form-params :action] "getExchangeRate"))
        data (request url options)]
    (:rate data)))

(defmethod get-exchange-rate :output [_]
  (let [options (-> default-options
                    (assoc :form-params
                           (merge (:form-params default-options)
                                  {:output "Y"}))
                    (assoc-in [:form-params :action] "getExchangeRate"))
        data (request url options)]
    (:rate data)))

(defn- base-pay
  [payment-system-id amount currency account payout-type]
  (let [options (-> default-options
                    (assoc :form-params
                           (merge (:form-params default-options)
                                  {:ps payment-system-id
                                   :curIn currency
                                   :sumOut amount
                                   :curOut currency
                                   :param_ACCOUNT_NUMBER account}))
                    (assoc-in [:form-params :action] payout-type))
        data (request url options)]
    data))

(defn init-pay
  "Get payout avalability"
  [payment-system-id amount currency account]
  (base-pay payment-system-id amount currency account "initOutput"))

(defn pay
  "Make real payout"
  [payment-system-id amount currency account]
  (base-pay payment-system-id amount currency account "output"))
