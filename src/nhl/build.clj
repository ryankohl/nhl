(ns nhl.build
  (:use [seabass core])
  (:require [nhl.buildRaw :as raw]
            [nhl.buildEnhanced :as enh]
            [nhl.buildGameSummaries :as games]))

(defn game-bin [v] [(first v) (last v)])
(defn build-model [n m]
  (apply build (pmap #(-> %
                           game-bin
                           raw/build-model
                           enh/build-model
                           games/build-model)
                      (partition-all 5 (range n (+ m 1))))))