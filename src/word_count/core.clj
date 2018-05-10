(ns word-count.core
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [word-count.file-reader :as f-reader]
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

(defn split-words-seq [line-seq]
  (->> (map split-words line-seq)
       (mapcat identity)))


(defn simple-split
  "The simplest way to count the words from file"
  [file]
  (with-open [f-reader (io/reader file)]
    (-> (line-seq f-reader)
        (split-words-seq)
        count)))

(defn nice-char? [ch]
  (let [ascii (int ch)]
    (or
      (and (>= ascii (int \a)) (<= ascii (int \z)))
      (and (>= ascii (int \A)) (<= ascii (int \Z)))
      (and (>= ascii (int \0)) (<= ascii (int \9))))))

(defn split-char-seq
  "Takes"
  [w-seq]
  (->> (mapcat identity w-seq)
       (filter nice-char?)))

(defn stats-split
  "Word count with additional statistics"
  [file]
  (with-open [f-reader (io/reader file)]
    (let [l-seq (line-seq f-reader)
          w-seq (split-words-seq l-seq)]
      (-> {}
          (assoc :lines (count l-seq))
          (assoc :words (count w-seq))
          (assoc :chars (count (split-char-seq w-seq)))))))

(defn frequency-split
  [file]
  (with-open [f-reader (io/reader file)]
    (->> (line-seq f-reader)
         split-words-seq
         (group-by identity)
         (map (fn [[word group]] [word (count group)])))))

(defn parallel-split [file]
  (ay/<!!
    (-> (f-reader/line-reader-chan file)
        split-words-async
        word-count-async)))

(defmulti dispatch-cmd first :default "--word-count")

(defmethod dispatch-cmd "--word-count" [[_ file-name]]
  (simple-split file-name))

(defmethod dispatch-cmd "--all" [[_ file-name]]
  (stats-split file-name))

(defmethod dispatch-cmd "--frequencies" [[_ file-name]]
  (frequency-split file-name))

(defmethod dispatch-cmd "--parallel" [[_ file-name]]
  (parallel-split file-name))




(defn -main
  [& args]
  (let [cmd (if (= 1 (count args)) (concat ["--word-count"] args) args)]
    (prn (dispatch-cmd cmd))))

