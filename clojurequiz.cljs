(ns clojurequiz
  (:require
   [promesa.core :as p]
   ["chalk$default" :as chalk]
   ["chalk-animation$default" :refer [rainbow]]
   ["inquirer$default" :as inquirer]
   ["console$log" :as log]))

(def player-name (atom ""))

(defn sleep
  ([] (sleep 2000))
  ([ms] (js/Promise. (fn [resolve] (js/setTimeout resolve ms)))))

(defn welcome []
  (p/let [rainbow-title (rainbow "Who Wants To Be A Clojure Millionaire?\n")]
    rainbow-title
    (sleep)
    (.stop rainbow-title)
    (log (str (chalk/bgBlue "HOW TO PLAY\n")
              "I am a process on your computer.\n"
              "If you get any question wrong I will be " (chalk/bgRed "killed\n")
              "So get all the questions right..."))))

(defn ask-name []
  (p/let [answers (inquirer/prompt #js {:name "player_name"
                                        :type "input"
                                        :message "What is your name?"
                                        :default (fn [] "Player")})]
    (reset! player-name (.-player_name answers))
    (log @player-name)))

(p/do! (welcome)
       (ask-name))
