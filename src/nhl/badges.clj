(ns nhl.badges
    (:use [seabass core]))

;; Log 50 or more hits and no violent penalties
(def clean-hitters "
prefix : <http://www.nhl.com/>
select ?game ?team
{
 {?game :hometeam ?team .
  ?game :homeHits ?hits .
  filter (?hits >= 50) .
  ?game :homeViolentPim 0 }
 union
 {?game :awayteam ?team .
  ?game :awayHits ?hits .
  filter (?hits >= 50) .
  ?game :awayViolentPim 0 }
}
")

;; Opposing team scored no goals
(def shut-em-out "
prefix : <http://www.nhl.com/>
select ?game ?team
{
 {?game :hometeam ?team .
  ?game :awayGoals 0 }
 union
 {?game :awayteam ?team .
  ?game :homeGoals 0 }
}
")

;; Opposing team made 10 shots or less
(def shut-em-down "
prefix : <http://www.nhl.com/>
select ?game ?team
{
 {?game :hometeam ?team .
  ?game :awayShots ?shots .
  filter (?shots <= 10) }
 union
 {?game :awayteam ?team .
  ?game :homeShots ?shots .
  filter (?shots <= 10) }
 }
")

;; Get hit 50 times or more in a game
(def check-magnets "
prefix : <http://www.nhl.com/>
select ?game ?team
{
 {?game :hometeam ?team .
  ?game :awayHits ?hits .
  filter (?hits >= 50 )}
 union
 {?game :awayteam ?team .
  ?game :homeHits ?hits .
  filter (?hits >= 50 )}
}
")

;; Log 20 minutes or more of fighting penalites
(def fight-club "
prefix : <http://www.nhl.com/>
select ?game ?team
{
 {?game :hometeam ?team .
  ?game :homeFights ?pim .
  filter (?pim >= 20) }
 union
 {?game :awayteam ?team .
  ?game :awayFights ?pim .
  filter (?pim >= 20) }
}
")

;; Log 30 minutes or more of violent penalties
(def war-pigs "
prefix : <http://www.nhl.com/>
select ?game ?team
{
 {?game :hometeam ?team .
  ?game :homeViolentPim ?pim .
  filter (?pim >= 30) }
 union
 {?game :awayteam ?team .
  ?game :awayViolentPim ?pim .
  filter (?pim >= 30) }
}
")

;; Double or better the opposing team's # of hits
(def this-is-sparta "
prefix : <http://www.nhl.com/>
select ?game ?team
{
 {?game :hometeam ?team .
  ?game :homeHits ?homeHits .
  ?game :awayHits ?awayHits .
  filter ((?awayHits * 2) <= ?homeHits)}
}
")

;; Win by a margin of 5 goals or more
(def on-a-showboat "
prefix : <http://www.nhl.com/>
select ?game ?team
{
 {?game :hometeam ?team .
  ?game :homeGoals ?win .
  ?game :awayGoals ?lose .
  filter ((?lose + 5) <= ?win)
  }
 union
 {?game :awayteam ?team .
  ?game :awayGoals ?win .
  ?game :homeGoals ?lose .
  filter ((?lose + 5) <= ?win)
  }
}
")

(defn get-stats [m]
  (let [bounce-it (fn [x] (bounce x m))
        get-stats (fn [x] (-> x bounce-it :rows))]
    {"clean hitters" (get-stats clean-hitters),
     "shut 'em out" (get-stats shut-em-out),
     "shut 'em down" (get-stats shut-em-down),
     "check magnets" (get-stats check-magnets),
     "fight club" (get-stats fight-club),
     "war pigs" (get-stats war-pigs),
     "this is sparta" (get-stats this-is-sparta),
     "we're on a showboat" (get-stats on-a-showboat)}))

