(ns cedarpelta.core
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [markov.core :as m]
            [twitter.api.restful :as t]
            [twitter.callbacks :refer :all]
            [twitter.callbacks.handlers :refer :all]
            [twitter.oauth :as t.oauth])
  (:gen-class))

; Note we don't assume any :user-timeline
(def default-source {:tweets-per-api-call 200
                     :max-recursion-depth 17})

; Note we don't assume any :twitter-credentials
(def default-configuration {:source default-source
                            :words-per-tweet [7 30]})

(def configuration (merge default-configuration
                          (-> "config.edn"
                              io/resource
                              slurp
                              edn/read-string)))

(def twitter-credentials
  (let [{:keys [consumer-key consumer-secret access-token access-token-secret]} (:twitter-credentials configuration)]
    (t.oauth/make-oauth-creds consumer-key consumer-secret access-token access-token-secret)))

(def timeline-parameters
  (let [{:keys [user-timeline tweets-per-api-call]} (:source configuration)]
    {:screen-name user-timeline
     :trim-user true
     :exclude-replies false
     :include-rts false
     :count tweets-per-api-call}))

(defn get-source-tweets
  ([] (get-source-tweets
        (get-in configuration [:source :max-recursion-depth])
        timeline-parameters))
  ([level timeline-params]
   (if (<= level 0)
     nil
     (let [tweets (:body (t/statuses-user-timeline :oauth-creds twitter-credentials :params timeline-params))
           tweet-ids (map :id tweets)
           oldest-tweet-id (apply min tweet-ids)
           tweet-texts (map :text tweets)
           next-level  (dec level)]
       (if (= 0 (count tweets))
         nil
         (concat tweet-texts (get-source-tweets next-level (assoc timeline-params :max-id oldest-tweet-id))))))))

(defn get-filtered-tweets []
  (filter
    #(not (or
            (re-matches #"@\S+" %)
            (re-matches #"http\S+" %)))
    (mapcat #(string/split % #"\s+") (get-source-tweets))))

(defn -main
  [& args]
  (let [probs (m/build-from-coll (get-filtered-tweets))
        [min-words-per-tweet max-words-per-tweet] (:words-per-tweet configuration)
        words-for-tweet (+ min-words-per-tweet (rand-int (- max-words-per-tweet min-words-per-tweet)))
        tweet-words (take words-for-tweet (m/generate-walk probs))
        joined-tweet (string/join " " tweet-words)
        truncated-tweet (if (<= (.length joined-tweet) 140)
                          joined-tweet
                          (subs joined-tweet 0 (string/last-index-of joined-tweet " " 139)))]
    (t/statuses-update :oauth-creds twitter-credentials :params {:status truncated-tweet})))
