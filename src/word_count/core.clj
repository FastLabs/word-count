(ns word-count.core
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.core.async :as ay]))

(defn- split-words [sentence]
  (str/split sentence #"\s+"))

(defn- inc-item [acc item]
  (update acc item #(if % (inc %) 1)))

(defn split-words-async [sentence-chan]
  (let [word-chan (ay/chan 100 (comp (map split-words)
                                     (mapcat identity)))]
    (ay/pipe sentence-chan word-chan)))

(defn word-count-async [word-chan]
  (ay/reduce inc-item {} word-chan))

(defn test-split []
  (let [split-chan (ay/chan)
        res-chan (-> split-chan
                     split-words-async
                     word-count-async)]
    (ay/go-loop []
      (when-let [result (ay/<! res-chan)]
        (prn result)
        (recur)))
    (when (ay/put! split-chan "abc cde efg hij abc")
      (ay/close! split-chan))))


(defn simple-split [file]
  (prn "simple split" file))

(defn stats-split [file]
  (prn "stats split" file))

(defn parallel-split [file]
  (prn "parallel split" file))

(defmulti dispatch-cmd first :default "--word-count")
(defmethod dispatch-cmd "--word-count" [[_ file-name]]
  (simple-split file-name))
(defmethod dispatch-cmd "--all" [[_ file-name]]
  (stats-split file-name))
(defmethod dispatch-cmd "--parallel" [[_ file-name]]
  (parallel-split file-name))




(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [cmd (if (= 1 (count args) ) (concat ["--word-count"] args) args)]
    (dispatch-cmd cmd)))

