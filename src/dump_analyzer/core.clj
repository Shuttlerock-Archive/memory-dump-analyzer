(ns dump-analyzer.core
  (:require [dump-analyzer.parsing :refer [parse-dump]]
            [dump-analyzer.analysis :refer [analyze-triad]]
            [fipp.clojure :refer (pprint) :rename {pprint fipp}])
  (:gen-class))

(defn inform
  [s]
  (.println *err* (str "... " s)))

(defn -main
  "Load three dumps and do a diff"
  [f1 f2 f3]
  (let [_  (inform "Loading dump 1")
        d1 (parse-dump f1)
        _  (inform "Loading dump 2")
        d2 (parse-dump f2)
        _  (inform "Loading dump 3")
        d3 (parse-dump f3)]

    (inform "Analyzing")
    (->> (analyze-triad d1 d2 d3)
         (select-keys d2)
         (fipp))))
