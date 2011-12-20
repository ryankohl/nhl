(ns nhl.server
  (:use noir.core)
  (:require [noir.server :as server]))

(defpage "/" [] (slurp "html/index.html"))
(defpage "/data" [] (slurp "files/results.json"))

(defn -main [& m]
  (server/start 8080))