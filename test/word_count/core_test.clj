(ns word-count.core-test
  (:require [clojure.test :refer :all]
            [word-count.core :as word-count]
            [word-count.core :refer :all]
            [clojure.java.io :as io]))

(deftest simple-split-count
  (testing "word count for some-lines.txt file"
    (is (= 4 (word-count/simple-split (io/resource  "test-data/some-lines.txt"))))))

(deftest filter-char-test
  (testing "filtering edges"
    (is (word-count/nice-char? \a))
    (is (word-count/nice-char? \z))
    (is (word-count/nice-char? \A))
    (is (word-count/nice-char? \Z))
    (is (word-count/nice-char? \0))
    (is (word-count/nice-char? \9)))

  (testing "middle combinations"
    (is (word-count/nice-char? \b))
    (is (word-count/nice-char? \1))
    (is (word-count/nice-char? \D))))

(deftest frequency-split-test
  (testing "frequency"
    (is (= [["first" 1] ["line" 2] ["second" 1]]
           (word-count/frequency-split (io/resource "test-data/frequency-split.txt"))))))


(deftest parallel-split-test
  (testing "parallel word count"
    (is (= {"first" 1 "line" 2 "second" 1} (word-count/parallel-split (io/resource  "test-data/some-lines.txt"))))))


