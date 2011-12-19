(ns nhl.teamStats
    (:use [seabass core]))

;; Top 5 teams hit by the subject
(defn hits-outgoing [subj]
  (str "
prefix : <http://www.nhl.com/>
select ?team ?avg
{
 { select ?team (avg (?hits) as ?avg)
  {?game :hometeam " subj " . ?game :awayteam ?team .
   ?game :homeHits ?hits}
  group by ?team ?game
 }
 union
 { select ?team (avg (?hits) as ?avg)
  {?game :awayteam " subj " . ?game :hometeam ?team .
   ?game :awayHits ?hits}
  group by ?team ?game
 }
}
order by desc(?avg)
limit 5
"))

;; Top 5 teams hitting the subject
(defn hits-incoming [subj]
  (str "
prefix : <http://www.nhl.com/>
select ?team ?avg
{
 { select ?team (avg (?hits) as ?avg)
  {?game :hometeam " subj " . ?game :awayteam ?team .
   ?game :awayHits ?hits}
  group by ?team ?game
 }
 union
 { select ?team (avg (?hits) as ?avg)
  {?game :awayteam " subj " . ?game :hometeam ?team .
   ?game :homeHits ?hits}
  group by ?team ?game
 }
}
order by desc(?avg)
limit 5
")) 

;; Top 5 teams the subject commits the most penalties against
(defn penalties-outgoing [subj]
  (str "
prefix : <http://www.nhl.com/>
select ?team ?avg
{
 { select ?team (avg (?pim) as ?avg)
  {?game :hometeam " subj " . ?game :awayteam ?team .
   ?game :homePim ?pim}
  group by ?team ?game
 }
 union
 { select ?team (avg (?pim) as ?avg)
  {?game :awayteam " subj " . ?game :hometeam ?team .
   ?game :awayPim ?pim}
  group by ?team ?game
 }
}
order by desc(?avg)
limit 5
")) 

;; Top 5 teams committing the most penalties when playing the subject
(defn penalties-incoming [subj]
  (str "
prefix : <http://www.nhl.com/>
select ?team ?avg
{
 { select ?team (avg (?pim) as ?avg)
  {?game :hometeam " subj " . ?game :awayteam ?team .
   ?game :awayPim ?pim}
  group by ?team ?game
 }
 union
 { select ?team (avg (?pim) as ?avg)
  {?game :awayteam " subj " . ?game :hometeam ?team .
   ?game :homePim ?pim}
  group by ?team ?game
 }
}
order by desc(?avg)
limit 5
")) 

;; Top 5 teams giving up shots to the subject
(defn shots-outgoing [subj]
  (str "
prefix : <http://www.nhl.com/>
select ?team ?avg
{
 { select ?team (avg (?shots) as ?avg)
  {?game :hometeam " subj " . ?game :awayteam ?team .
   ?game :homeShots ?shots}
  group by ?team ?game
 }
 union
 { select ?team (avg (?shots) as ?avg)
  {?game :awayteam " subj " . ?game :hometeam ?team .
   ?game :awayShots ?shots}
  group by ?team ?game
 }
}
order by desc(?avg)
limit 5
")) 

;; Top 5 teams getting off shots on the subject
(defn shots-incoming [subj]
  (str "
prefix : <http://www.nhl.com/>
select ?team ?avg
{
 { select ?team (avg (?shots) as ?avg)
  {?game :hometeam " subj " . ?game :awayteam ?team .
   ?game :awayShots ?shots}
  group by ?team ?game
 }
 union
 { select ?team (avg (?shots) as ?avg)
  {?game :awayteam " subj " . ?game :hometeam ?team .
   ?game :homeShots ?shots}
  group by ?team ?game
 }
}
order by desc(?avg)
limit 5
")) 

;; Top 5 teams getting scored on by the subject
(defn goals-outgoing [subj]
  (str "
prefix : <http://www.nhl.com/>
select ?team ?avg
{
 { select ?team (avg (?goals) as ?avg)
  {?game :hometeam " subj " . ?game :awayteam ?team .
   ?game :homeGoals ?goals}
  group by ?team ?game
 }
 union
 { select ?team (avg (?goals) as ?avg)
  {?game :awayteam " subj " . ?game :hometeam ?team .
   ?game :awayGoals ?goals}
  group by ?team ?game
 }
}
order by desc(?avg)
limit 5
")) 

;; Top 5 teams scoring on the subject
(defn goals-incoming [subj]
  (str "
prefix : <http://www.nhl.com/>
select ?team ?avg
{
 { select ?team (avg (?goals) as ?avg)
  {?game :hometeam " subj " . ?game :awayteam ?team .
   ?game :awayGoals ?goals}
  group by ?team ?game
 }
 union
 { select ?team (avg (?goals) as ?avg)
  {?game :awayteam " subj " . ?game :hometeam ?team .
   ?game :homeGoals ?goals}
  group by ?team ?game
 }
}
order by desc(?avg)
limit 5
"))

(def team-names "
prefix : <http://www.nhl.com/> 
select distinct ?team ?name
{ ?team :name ?name }
")

(def team-list "
prefix : <http://www.nhl.com/>
select distinct ?team
{ ?team :name ?name}
")

(defn get-stats [m]
  (let [bounce-it (fn [x] (bounce x m))
        teams-rs  (bounce team-list m)
        teams (map #(str "<" (:team %) ">") (:rows teams-rs))
        names-rs (bounce team-names m)
        names (zipmap (map :team (:rows names-rs))
                      (map :name (:rows names-rs)))
        get-stats (fn [x] (zipmap teams (map #(-> % x bounce-it :rows) teams)))]
    {"hits-out" (get-stats hits-outgoing),
     "hits-in" (get-stats hits-incoming),
     "pens-out" (get-stats penalties-outgoing),
     "pens-in" (get-stats penalties-incoming),
     "shots-out" (get-stats shots-outgoing),
     "shots-in" (get-stats shots-incoming)
     "goals-out" (get-stats goals-outgoing)
     "goals-in" (get-stats goals-incoming)
     "team-names" names}))