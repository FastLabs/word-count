(ns word-count.file-reader-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [word-count.file-reader :refer [line-reader-chan]]
            [clojure.core.async :as ay]))

(deftest io-test
  (testing "if i can read the file"
    (let [input-file (io/resource "test-data/some-lines.txt")
          lines (ay/<!! (ay/into [] (line-reader-chan input-file)))]
      (is (= 2 (count lines))))))


