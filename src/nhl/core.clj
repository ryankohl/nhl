(ns nhl.core
  (:use [seabass core])
  (:require [nhl.download :as downloader]
            [nhl.parse :as parser]
            [nhl.build :as builder]
            [nhl.teamStats :as teams]
            [nhl.badges :as badges]
            [clojure.string :as string]
            [clojure.contrib.json :as json]))

(comment "download the first 20 games for the 2010 season") 
(downloader/download-data 1 20 2010)

(comment "transform the first 20 games from json to rdf")
(parser/parse 1 20)

(comment "process each game, extracting the relevant
triples, and put them in a model")
(def game-data (builder/build-model 1 20))

(comment "submit a query for the result set")
(def result-set {"teamData" (teams/get-stats game-data),
                 "badgeData" (badges/get-stats game-data)})

(comment "transform the result set into json, clean the
'\\' strings out,  and save to file")
(def json-data-dirty (json/json-str result-set))
(def json-data (clojure.string/replace json-data-dirty "\\" ""))
(spit "files/results.json" json-data)
