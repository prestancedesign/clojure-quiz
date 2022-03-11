(ns clojurequiz
  (:require
   [promesa.core :as p]
   ["chalk$default" :as chalk]
   ["chalk-animation$default" :refer [rainbow]]
   ["inquirer$default" :as inquirer]
   ["gradient-string$default" :as gradient]
   ["figlet$default" :as figlet]
   ["console$log" :as log]
   ["nanospinner$default" :refer [createSpinner]]))

(def player-name (atom ""))

(defn sleep
  ([] (sleep 2000))
  ([ms] (js/Promise. (fn [resolve] (js/setTimeout resolve ms)))))

(defn welcome []
  (p/let [rainbow-title (rainbow "Who Wants To Be A Clojure Millionaire? \n")]
    rainbow-title
    (sleep)
    (.stop rainbow-title)
    (log (str (chalk/bgBlue "HOW TO PLAY\n")
              "I am a process on your computer.\n"
              "If you get any question wrong I will be " (chalk/bgRed "killed\n")
              "So get all the questions right..."))))

(defn handle-answer [is-correct]
  (p/let [spinner (.start (createSpinner "Checking answer..."))]
    (sleep 1000)
    (if is-correct
      (.success spinner #js {:text (str "Nice work " @player-name ". That's a legit answer")})
      (do
        (.error spinner #js {:text (str "💀💀💀 Game over, you lose " @player-name "!")})
        (.exit js/process 1)))))

(defn ask-name []
  (p/let [answers (inquirer/prompt #js {:name "player_name"
                                        :type "input"
                                        :message "What is your name?"
                                        :default (fn [] "Player")})]
    (reset! player-name (.-player_name answers))))

(defn winner []
  (js/console.clear)
  (figlet (str "Congrats , " @player-name " !\n $ 1 , 0 0 0 , 0 0 0")
          (fn [err data]
            (log (.multiline (.-pastel gradient) data) "\n")
            (log (.green chalk "Programming isn't about what you know; it's about making the command line look cool")))))

(defn question-1 []
  (p/let [answers (inquirer/prompt (clj->js {:name "question_1"
                                             :type "list"
                                             :message "When was Clojure released?\n"
                                             :choices ["May 23rd, 2011"
                                                       "Oct 16th, 2007"
                                                       "Dec 4th, 2005"
                                                       "Dec 17, 1996"]}))]
    (handle-answer (= (.-question_1 answers) "Oct 16th, 2007"))))

(defn question-2 []
  (p/let [answers (inquirer/prompt (clj->js {:name "question_2"
                                             :type "list"
                                             :message "Who designed Clojure\n"
                                             :choices ["Yukihiro Matsumoto"
                                                       "John Clojure"
                                                       "Rich Hickey"
                                                       "James Gosling"]}))]
    (handle-answer (= (.-question_2 answers) "Rich Hickey"))))

(defn question-3 []
  (p/let [answers (inquirer/prompt (clj->js {:name "question_3"
                                             :type "list"
                                             :message "Which of these is not a file extension for Clojure?\n"
                                             :choices ["clj"
                                                       "cljc"
                                                       "ens"
                                                       "edn"]}))]
    (handle-answer (= (.-question_3 answers) "ens"))))

(defn question-4 []
  (p/let [answers (inquirer/prompt (clj->js {:name "question_4"
                                             :type "list"
                                             :message "Which of these platforms is not used to run Clojure?\n"
                                             :choices ["Java Virtual Machine"
                                                       "Common Language Runtime"
                                                       "Commodore 64"
                                                       "JavaScript"]}))]
    (handle-answer (= (.-question_4 answers) "HyperText Markup Language"))))

(js/console.clear)

(p/do! (welcome)
       (ask-name)
       (question-1)
       (question-2)
       (question-3)
       (question-4)
       (winner))
