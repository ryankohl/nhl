(ns nhl.buildEnhanced
  (:use [seabass core]))

(defn penaltyDef [pen match]
  (str "prefix nhl: <http://www.nhl.com/> "
       "construct { ?e a nhl:" pen "} "
       " { ?e a nhl:Penalty . ?e nhl:desc ?d . FILTER regex(?d, '.*("
       match
       ").*')}"))

(def boarding (penaltyDef "Boarding" "BOARDING"))
(def charging (penaltyDef "Charging" "CHARGING"))
(def cross-checking (penaltyDef "CrossChecking" "CROSS CHECKING"))
(def delay-of-game (penaltyDef "DelayOfGame" "DELAYING GAME-PUCK OVER GLASS"))
(def elbowing (penaltyDef "Elbowing" "ELBOWING"))
(def fighting-maj (penaltyDef "FightingMaj" "FIGHTING (MAJ)"))
(def highsticking (penaltyDef "HighSticking" "HI-STICKING"))
(def highsticking-dm (penaltyDef "HighSticking-DoubleMinor" "HI-STICK - DOUBLE MINOR"))
(def holding (penaltyDef "Holding" "HOLDING"))
(def hooking (penaltyDef "Hooking" "HOOKING"))
(def interference (penaltyDef "Interference" "INTERFERENCE"))
(def misconduct (penaltyDef "Misconduct" "MISCONDUCT (10 MIN)"))
(def roughing (penaltyDef "Roughing" "ROUGHING"))
(def slashing (penaltyDef "Slashing" "SLASHING"))
(def tripping (penaltyDef "Tripping" "TRIPPING"))
(def unsportsmanlike (penaltyDef "Unsportsmanlike" "UNSPORTSMANLIKE CONDUCT"))

(def ont "files/ontology.ttl")
(def rules "files/nhl.rules")

(defn build-model [m]
  (let [penalties [boarding charging cross-checking delay-of-game elbowing
                   fighting-maj highsticking highsticking-dm holding
                   hooking interference misconduct roughing slashing
                   tripping unsportsmanlike]
        penaltyModel (apply build (map #(pull % m) penalties))]
    (build m ont rules penaltyModel)))

