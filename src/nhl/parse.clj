(ns nhl.parse
  (:require [clojure.string]
	    [clojure.contrib [json :as json]]
	    [seabass [utils :as sbutil]]))

(defn nope? [x] (or (= "" x) (= nil x)))
(defn valid? [dt val] (and (not (nope? val)) (sbutil/valid-dt? dt val)))
(defn blank [id] (str "_:b" id) )
(defn node [namespace id] (if-not (or (nope? namespace) (nope? id)) (str "<" namespace id ">") ))
(defn bool-lit [val] (if (valid? "boolean" (str val)) (str \" val \" "^^<http://www.w3.org/2001/XMLSchema#boolean>") ))
(defn date-lit [val] (if (valid? "date" (str val)) (str \" val \" "^^<http://www.w3.org/2001/XMLSchema#date>") ))
(defn datetime-lit [val] (if (valid? "datetime" (str val)) (str \" val \" "^^<http://www.w3.org/2001/XMLSchema#dateTime>") ))
(defn decimal-lit [val] (if (valid? "decimal" (str val)) (str \" val \" "^^<http://www.w3.org/2001/XMLSchema#decimal>") ))
(defn decimal-lit [val] (if (valid? "double" (str val)) (str \" val \" "^^<http://www.w3.org/2001/XMLSchema#double>") ))
(defn decimal-lit [val] (if (valid? "float" (str val)) (str \" val \" "^^<http://www.w3.org/2001/XMLSchema#float>") ))
(defn duration-lit [val] (if (valid? "duration" (str val)) (str \" val \" "^^<http://www.w3.org/2001/XMLSchema#duration>") ))
(defn int-lit [val] (if (valid? "integer" (str val)) (str \" val \" "^^<http://www.w3.org/2001/XMLSchema#integer>") ))
(defn str-lit [val] (if (valid? "string" (str val)) (str \" val \") ))
(defn time-lit [val] (if (valid? "time" (str val)) (str \" val \" "^^<http://www.w3.org/2001/XMLSchema#time>") ))
(defn fact [s p o] (if-not (or (nope? s) (nope? p) (nope? o)) (println (clojure.string/join " " [s p o]) " .")))

(def rdf  "http://www.w3.org/1999/02/22-rdf-syntax-ns#")
(def rdfs "http://www.w3.org/2000/01/rdf-schema#")
(def nhl "http://www.nhl.com/")
(def nhlg "http://www.nhl.com/game-")
(def *gid* (atom 0))

(defn fixLocalTime [time]
  (let [t (clojure.string/split time #":")
	h (str (+ (Integer/parseInt (first t)) 12))
	m (clojure.string/replace (str (first (rest t))) " PM" "")
	s "00" ]
    (clojure.string/join ":" [h m s])))

(defn print-play [play]
  (let [pid1 (if-not (nope? (:pid1 play)) (str "pid-" (:pid1 play)))
	pid2 (if-not (nope? (:pid2 play)) (str "pid-" (:pid2 play)))
	pid3 (if-not (nope? (:pid3 play)) (str "pid-" (:pid3 play)))
	tid (if-not (nope? (:teamid play)) (str "team-" (:teamid play)))
	eid (if-not (nope? (:formalEventId play)) (str "eid-" (clojure.string/replace (:formalEventId play) "\\" "")))
	etyp (if-not (nope? (:type play)) (clojure.string/replace (:type play) "\\" "")) ]
    (fact (node nhlg @*gid*) (node nhl "play") (node nhl eid))
    (fact (node nhl eid) (node rdf "type") (node nhl etyp))
    (fact (node nhl eid) (node nhl "desc") (str-lit (:desc play)))
    (fact (node nhl eid) (node nhl "agent1") (node nhl pid1))
    (fact (node nhl eid) (node nhl "agent2") (node nhl pid2))
    (fact (node nhl eid) (node nhl "agent3") (node nhl pid3))
    (fact (node nhl pid1) (node rdfs "label") (str-lit (:p1name play)))
    (fact (node nhl pid2) (node rdfs "label") (str-lit (:p2name play)))
    (fact (node nhl pid3) (node rdfs "label") (str-lit (:p3name play)))
    (fact (node nhl eid) (node nhl "team") (node nhl tid))
    (fact (node nhl eid) (node nhl "lat") (int-lit (:xcoord play)))
    (fact (node nhl eid) (node nhl "lon") (int-lit (:ycoord play)))
    (fact (node nhl eid) (node nhl "localtime") (time-lit (fixLocalTime (:localtime play))))
    (fact (node nhl eid) (node nhl "time") (time-lit (str "00:" (:time play))))
    (fact (node nhl eid) (node nhl "period") (int-lit (:period play)))))

(defn print-game [game]
  (let [home (if-not (nope? (:hometeamid game)) (str "team-" (:hometeamid game)))
	away (if-not (nope? (:awayteamid game)) (str "team-" (:awayteamid game)))	]
    (swap! *gid* inc)
    (fact (node nhlg @*gid*) (node nhl "hometeam") (node nhl home))
    (fact (node nhlg @*gid*) (node nhl "awayteam") (node nhl away))
    (fact (node nhl home) (node rdfs "label") (str-lit (:hometeamname game)))
    (fact (node nhl away) (node rdfs "label") (str-lit (:awayteamname game)))
    (doseq [p (:play (:plays game))] (print-play p))))

(defn parse [n m]
  (doseq [ i (range n (+ m 1))]
    (spit (str "data/file-" i ".nt")
	  (with-out-str (print-game (:game (:data (json/read-json (slurp (str "raw/file-" i ".json"))))))))))