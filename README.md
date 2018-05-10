# word-count


# this is what I try to solve:

WordCount accepts zero or one command line options. Consider them mutually exclusive (it is not necessary to implement a solution that accepts combinations of command line option, only the first one will be considered). WordCount should:
1. Output the total number of words in a file:
    $> echo "Very nice ad-hoc counting." > temp.txt
    $> wordcount temp.txt
    $> 5
Consider "word" any group of 1+ consecutive letters or numbers ([a-zA-Z0-9]) separated by something outside this set, like spaces or punctuation. For instance, "ad-hoc" are two words.
2. Output additional metrics, such as number of lines and characters:
    $> echo "Very nice counting. Really very nice!" > temp.txt
    $> wordcount --all temp.txt
    $> line:1, words:6, chars:30
Please only consider letters and numbers for character count ([a-zA-Z0-9]). Punctuation, spaces or other special characters should not be counted. Lines are separated by new-lines chars ("\n").
3. Show frequencies, like for example:
    $> echo "Very nice counting. Really very nice" > temp.txt
    $> wordcount --frequencies temp.txt
    $> ["nice" 2] ["very" 1] ["Really" 1] ["counting" 1] ["Very" 1]
In this case WordCount will be case sensitive. Use the same definition of word as point 1. 4. Have WordCount to run on concurrently on multi-core hardware to speed up processing
    $> curl http://www.gutenberg.org/cache/epub/2600/pg2600.txt > war-and-peace-full-book.txt
    $> wordcount --parallel war-and-peace-full-book.txt
    $> 566321
For the last point it is expected that >1 core of a multi-core machine is busy computing the word count. Most
modern languages contain some facility or library to achieve this. In case the selected language does not contain such a facility, it is okay to show that the word count is happening on multiple (green) threads.

## Assumptions
### Probably I need to re-organize the code but i will do it in next iteration
### Using core.async for concurrency
### Overall trying to implement word count problem without focusing on details such:

  * Not focusing on chunk/lines split
  * Not focusing on word split technics

## Installation
  lein uberjar

## Usage

### Run these commands to test

* task nr.1
  java -jar ./target/uberjar/word-count-0.1.0-SNAPSHOT-standalone.jar   ./resources/test-data/some-lines.txt

* task nr.2
  java -jar ./target/uberjar/word-count-0.1.0-SNAPSHOT-standalone.jar --all  ./resources/test-data/some-lines.txt

* task nr.3
  java -jar ./target/uberjar/word-count-0.1.0-SNAPSHOT-standalone.jar --frequencies  ./resources/test-data/some-lines.txt

* task nr.4
  java -jar ./target/uberjar/word-count-0.1.0-SNAPSHOT-standalone.jar --parallel  ./resources/test-data/some-lines.txt  


### TODO:

 - work with exception handling
 - bring some more structure to the code



## License

Copyright © 2018 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
