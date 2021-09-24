(ns app.auth-test
  (:require [clojure.test :refer :all]
            [app.auth :as auth]))

(deftest password-rules--test
  (testing "tesing validating password"
  (is (empty? (auth/password-rules "pAsswOrd@123")) true)
  (is (contains? (auth/password-rules "pAssw!") :password.error/too-short) true)
  (is (contains? (auth/password-rules "pAsswOrd123") :password.error/missing-special-character) true)
  (is (contains? (auth/password-rules "PASSWORD@123") :password.error/missing-lowercase) true)
  (is (contains? (auth/password-rules "password@123") :password.error/missing-uppercase) true)))

(deftest authenticate-user--test
  (testing "testing authenticate user"
    (let [db auth/user-database]
      (is (:id (auth/authenticate-user @db
                                       "defaultuser"
                                       "password@123")
            "defaultuser"))
      (is (nil? (auth/authenticate-user @db
                                        "defaultuser1"
                                        "password@123")) true)
      (is (thrown-with-msg? Exception
                            #"Invalid username or password"
                            (auth/authenticate-user @db
                                                    "defaultuser"
                                                    "password@12")) true))))

(deftest create-user--test
  (testing "testing creating user"
    (let [username (str "usertesting" str(rand-int 1000))]
    (is (:id (auth/create-user auth/user-database
                               username
                               "pAsswOrd@123")) username)
    (is (thrown-with-msg? Exception
                          #"User already exists"
                          (auth/create-user auth/user-database
                                            username
                                            "pAsswOrd@123")) true))))