(ns word-count.core-test
  (:require [clojure.test :refer :all]
            [word-count.core :as word-count]
            [word-count.core :refer :all]
            [clojure.java.io :as io]))

(deftest simple-split-count
  (testing "word count for some-lines.txt file"
    (is (= 4 (word-count/simple-split (io/resource  "test-data/some-lines.txt"))))))
