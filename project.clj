(defproject nhl "1.0.0"
  :description "An analysis of NHL play-by-play data with incanter and seabass"
  :dependencies [[org.clojure/clojure "1.2.0"]
		 [org.clojure/clojure-contrib "1.2.0"]
		 [seabass "0.3.3"]
		 [incanter "1.2.3"]    
		 [org.clojars.vgeshel/http.async.client "0.2.2"]
                 [noir "1.2.1"]]
  :main nhl.server)
