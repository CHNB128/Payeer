(ns payeer.core-test
  (:require
   [clojure.test :refer :all]
   [payeer.api :refer :all]))

(def creditionals
  {:account "P1017450858"
   :apiId "871039145"
   :apiPass "Cp59qRPj9Ns3btOd"})

(deftest auth-test
  (testing "Authorization"
    (is (= creditionals (set-creditionals! creditionals)))))

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
    (let [payment-system-id nil
          amount nil
          currency nil
          account nil])
    (is (not (nil? (init-pay payment-system-id amount currency account))))
    (is (not (nil? (pay payment-system-id amount currency account))))))
