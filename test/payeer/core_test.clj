(ns payeer.core-test
  (:require
   [clojure.test :refer :all]
   [payeer.core :refer :all]))

(def payeer-key "123")
(def signed-url "https://payeer.com/merchant/?m_shop=881000759&m_orderid=12345&m_amount=150.00&m_curr=USD&m_desc=VGVzdCBwYXltZW50IOKEljEyMzQ1&m_sign=D5874B7A845856BCC23FAF385A5AE1E0AD0F58E46C9A1B30C8DE329D4628204B&lang=en")

(deftest url-generation-test
  (testing "Generate merchant link"
    (let [url (generate-merchant-link
               {:shop-id "871014559"
                :payeer-key payeer-key}
               {:order-id 12345
                :amount 150
                :currency "USD"
                :description "Test payment №12345"}
               {:lang "en"})]
      (is (= url signed-url)))))
