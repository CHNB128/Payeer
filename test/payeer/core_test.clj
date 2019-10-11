(ns payeer.core-test
  (:require
   [clojure.test :refer :all]
   [payeer.core :refer :all]))

(def payeer-key "QUmxnAyF55cbf%G^2*&n8%yn#BwWNsz3GJSST2S@nZj4C4*ZxmiD&#7QyMY@#d6N8Weznj2*i$eKq2#D@%jwRm!AWda*#TEvW@X^r82z7%&FCC8W84YG8nNH4qjk$ras")

(deftest url-generation-test
  (testing "Generate merchant link"
    (let [url (generate-merchant-link
               {:shop-id "871014559"
                :order-id 12345
                :payeer-key payeer-key}
               {:amount 150
                :currency "USD"
                :description "Test payment №12345"}
               {:lang "en"})]
      (is (= url "https://payeer.com/merchant/?m_shop=871014559&m_orderid=12345&m_amount=150.00&m_curr=USD&m_desc=VGVzdCBwYXltZW50IOKEljEyMzQ1&m_sign=FB8A2D7DA3ED23569C6A39810BB5427C204E67C57E5AA72BCED0B5A893C12635&lang=en")))))
