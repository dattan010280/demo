(ns app.api-test
  (:require [clojure.test :refer :all]
            [app.api :as api]
            [ring.mock.request :as mock]))

(deftest authenticate-user-handler--test
  (is (api/authenticate-user-handler {:parameters {:query {:username "defaultuser" :password "password@123"}}})
      {:status 200, :body {:id "defaultuser", :role :user}}))

(deftest authenticate-user-handler-url--test
  (is (= (mock/request :get "http://localhost:3000/user?username=defaultuser&password=password@123")
         {:protocol "HTTP/1.1",
          :remote-addr "127.0.0.1",
          :headers {"host" "localhost:3000"},
          :server-port 3000,
          :uri "/user",
          :server-name "localhost",
          :query-string "username=defaultuser&password=password@123",
          :scheme :http,
          :request-method :get})))

(deftest create-user-handler--test
  (let [username (str "usertesting" str(rand-int 1000))]
    (is (api/create-user-handler {:parameters {:body {:username username :password "pAsswOrd@123"}}})
        {:status 200, :body {:id username, :role :user}})))

(deftest create-user-handler-url--test
  (is (:protocol (-> (mock/request :post "http://localhost:3000/user")
                     (mock/json-body {:username "demouser1" :password "abcdEFGH@123"}))
        "HTTP/1.1")))