(ns payeer.api-test
  (:require
   [clojure.test :refer :all]
   [payeer.api :refer :all]))

(deftest api-test
  (testing "Authorization"
    (let [creditionals
          {:account "P1017450858"
           :apiId "871039145"
           :apiPass "Cp59qRPj9Ns3btOd"}]
      (is (= creditionals (set-creditionals! creditionals)))))

  (testing "Get balance"
    (is (not (nil? (get-balance)))))

  (testing "Get payment systems"
    (is (not (nil? (get-payment-systems)))))

  (testing "Get history"
    (is (not (nil? (get-history)))))

  (testing "Get history"
    (is (not (nil? (get-exchange-rate :input))))
    (is (not (nil? (get-exchange-rate :output)))))

  (testing "Pay out"
    ; TODO: make auto generated params
    (let [payment-system-id 27313794
          amount 100
          currency "RUB"
          account "P1017450858"]
      (is (not (nil? (init-pay account payment-system-id amount currency)))))))
