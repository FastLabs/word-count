(ns word-count.file-reader
  (:require [clojure.core.async :as ay]
            [clojure.java.io :as io]))

(defn line-reader-chan
  ([file-name]
   (line-reader-chan 10 file-name))
  ([buff-size file]
   (let [line-chan (ay/chan buff-size)]
     (ay/thread
       (try
         (with-open [r (io/reader file)]
           (doseq [line (line-seq r)]
             (ay/put! line-chan line))
           (ay/close! line-chan))
         (catch Exception e
           (do (prn "Error when streaming lines from file" e)
               (ay/close! line-chan)))))
     line-chan)))
