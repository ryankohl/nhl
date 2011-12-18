(ns nhl.buildRaw
  (:use [seabass core]))

(defn get-data[n m] (map #(str "data/file-" % ".nt")
                         (range n (+ 1 m))))

(defn build-model [[n m]] (apply build (get-data n m)))