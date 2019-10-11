(ns payeer.core
  (:import
   java.util.Base64
   java.net.URLEncoder)
  (:require
   [url-utils.core :refer [encode-params]]
   [org.httpkit.client :as http]
   [clojure.string :as string]
   [digest :as digest]))

(defn encode
  [to-encode]
  (.encodeToString (Base64/getEncoder) (.getBytes to-encode)))

(defn format-amount
  [amount]
  (format "%.2f" (float amount)))

(defn sign [payload]
  (->> payload
       vals
       (string/join ":")
       digest/sha-256
       string/upper-case))

; (defprotocol Payeer
;   "The Payeer protocol"
;   (prepare [this] "add meta data to payload")
;   (sign [this] "Sign the payload")
;   (excute [this] "Excute payload"))

; (deftype Payload [amount currency description]
;   Payeer
;   (prepare [this]
;     (-> this
;         (assoc :m_shop (env :app-domain))
;         (assoc :m_orderid 1)
;         (assoc :m_amount (format-amount amount))
;         (assoc :m_curr currency)
;         (assoc :m_desc (encode description)
;         (assoc :m_key payeer-key)))

; (defn make-payload
;   [amount currency description]
;   {:m_shop (env :app-domain)
;    :m_orderid 1
;    :m_amount (format-amount amount)
;    :m_curr currency
;    :m_desc (encode description)
;    :m_key payeer-key})

; (defn make-order
;   [amount currency description]
;   (let [payload (make-payload amount currency description)
;         hash (sign payload)]
;     (let [options {:form-params (accos payload :m_sign hash)}
;           {:keys [status body error]} @(http/post "https://payeer.com/merchant/" options)]
;       (if error
;         (println "Failed, exception is " error)
;         (println status body)))))

(def url "https://payeer.com/merchant/")

(defn generate-merchant-link
  [{:keys [shop-id order-id payeer-key]}
   {:keys [amount currency description]}
   {:keys [lang]}]
  (let [wrap-sign #(assoc %1 :m_sign (sign (assoc %1 :m_key payeer-key)))
        make #(str url "?" (encode-params %1))]
    (-> {:m_shop shop-id
         :m_orderid order-id
         :m_amount (format-amount amount)
         :m_curr currency
         :m_desc (encode description)}
        wrap-sign
        make
        (str "&lang=" lang))))

(defn ips-valid [request]
  "Check if ip address in range 185.71.65.92, 185.71.65.189, 149.202.17.210"
  (throw (Exception. "not implemented")))

; (defn handler [request api-key callback]
;   (when (not (ips-valid request))
;     (throw (Exception. "Corrupted request")))
;   (when (and (not (nil? (:m_operation_id request)))
;              (not (nil? (:m_sign request))))
;     (-> []
;         (conj (:m_operation_id request))
;         (conj (:m_operation_ps request))
;         (conj (:m_operation_date request))
;         (conj (:m_operation_pay_date request))
;         (conj (:m_shop request))
;         (conj (:m_orderid request))
;         (conj (:m_amount request))
;         (conj (:m_curr request))
;         (conj (:m_desc request))
;         (conj (:m_status request))
;         #(when (not (nil? (:m_params request)))
;            (conj %1 (:m_params request)))
;         (conj api-key)
;         (sing)
;         #(if (and (= (:m_sign request) %1) (= (:m_status request "success")))
;            (callback request)
;            (throw (Exception. "Erron during processing request"))))))