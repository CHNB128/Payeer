(ns payeer.core-test
  (:require
   [clojure.test :refer :all]
   [payeer.api :refer :all]))

(deftest auth-test
  (testing "Authorization"
    (let [creditionals
          {:account "P1017450858"
           :apiId "871039145"
           :apiPass "Cp59qRPj9Ns3btOd"}]
      (is (= creditionals (set-creditionals! creditionals))))))

(deftest get-balance-test
  (testing "Get balance"
    (is (not (nil? (get-balance))))))

(deftest get-payment-systems-test
  (testing "Get payment systems"
    (is (not (nil? (get-payment-systems))))))

(deftest get-history-test
  (testing "Get history"
    (is (not (nil? (get-history))))))

(deftest get-exchange-rate-test
  (testing "Get history"
    (is (not (nil? (get-exchange-rate :input))))
    (is (not (nil? (get-exchange-rate :output))))))

(deftest pay-test
  (testing "Pay out"
    ; TODO: make auto generated params
    (let [payment-system-id 27313794
          amount 100
          currency "RUB"
          account "RUB"]
      (is (not (nil? (init-pay payment-system-id amount currency account)))))))
