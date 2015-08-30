(ns dump-analyzer.analysis
  (:require [clojure.set :refer [intersection difference]]))

(defn key-set
  "Returns keys of m as a set"
  [m]
  (into #{} (keys m)))

(defn analyze-triad
  "Takes d2, removes those objects which are present in d1
   and also those which are NOT present in d3. Returns keys which
   are left in d2 as an int-set."
  [d1 d2 d3]
  (let [ks1 (key-set d1)
        ks2 (key-set d2)
        ks3 (key-set d3)]

    (intersection (difference ks2 ks1) ks3)))