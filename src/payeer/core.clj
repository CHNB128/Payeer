(ns payeer.core
  (:import
   java.util.Base64
   java.net.URLEncoder)
  (:require
   [url-utils.core :refer [encode-params]]
   [org.httpkit.client :as http]
   [clojure.string :as string]
   [digest :as digest]))

; base url
(defonce url "https://payeer.com/merchant/")

; aproved ip adresses
(defonce ips ["185.71.65.92" "185.71.65.189" "149.202.17.210"])

(defn- encode
  [to-encode]
  (.encodeToString (Base64/getEncoder) (.getBytes to-encode)))

(defn- format-amount
  [amount]
  (format "%.2f" (float amount)))

(defn- sign
  "Hash map to string with ':' separated values "
  [payload]
  (->> payload
       vals
       (string/join ":")
       digest/sha-256
       string/upper-case))

(defn generate-merchant-link
  "Generate link for replanish balance"
  [{:keys [shop-id payeer-key]}
   {:keys [order-id amount currency description]}
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

(defn ips-valid
  "Check if ip address in range 185.71.65.92, 185.71.65.189, 149.202.17.210"
  [{:keys [remote-addr]}]
  (when-not (.indexOf ips remote-addr)
    (throw (Exception. "IP address of request out of range"))))

(defn handler
  "Handler for 'status' endpoint"
  [payeer-key {:keys [body] :as request} callback]
  (ips-valid request)
  (when-not (and (nil? (:m_operation_id body)) (nil? (:m_sign body)))
    (let [hash (-> (assoc body :m_key payeer-key) sign)]
      (if (and (= (:m_sign body) hash) (= (:m_status body) "success"))
        (callback "success" (:m_orderid body))
        (callback "fail" (:m_orderid body))))))
