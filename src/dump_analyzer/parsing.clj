(ns dump-analyzer.parsing
  (:require [cheshire.core :as cheshire]
            [clojure.java.io :as io]
            [clojure.data.int-map :as i]))

(defn parse-address
  "Converts address in 0x7f30434af658 form to Long"
  [^String s]
  (Long/parseLong (.substring s 2) 16))

(defn apply-to-key
  "Replaces (m k) with (f (m k)) in map m, if key k exists"
  [m k f]
  (if-let [v (get m k)]
    (assoc! m k (f v))
    m))

(defn unhex
  "Replaces hex string under key in item with parsed version of it"
  [item key]
  (apply-to-key item key parse-address))

(defn unhex-vector
  [item key]
  (apply-to-key item key #(mapv parse-address %)))

(defn prepare-item
  "Prepare parsed item for insertion"
  [item]
  (-> item
      transient
      (unhex :address)
      (unhex :class)
      (unhex-vector :references)
      (persistent!)))

(defn parse-dump
  [filename]
  (with-open [rdr (io/reader (io/file filename))]
    (loop [lseq  (line-seq rdr)
           items (transient (i/int-map))]

      (if-let [line (first lseq)]
        (let [p (cheshire/parse-string line true)
              adr (:address p)
              lseq (rest lseq)]
          (recur lseq (if adr
                        (assoc! items (parse-address adr)
                                (prepare-item p))
                        items)))
        (persistent! items)))))
