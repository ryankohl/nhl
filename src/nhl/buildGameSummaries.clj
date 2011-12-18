(ns nhl.buildGameSummaries
  (:use [seabass core]))

(def team-names "
prefix : <http://www.nhl.com/> 
prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
construct { ?team :name ?name }
{ ?team a :Team . ?team rdfs:label ?name }
")

(def hometeam-hits "
prefix : <http://www.nhl.com/>
construct { ?game :homeHits ?hits }
{ select ?game (count (?hit) as ?hits)
 {?game a :Game . ?game :hometeam ?team .
  ?game :play ?hit . ?hit a :Hit . ?hit :team ?team
 }
 group by ?game ?team
}
")

(def awayteam-hits "
prefix : <http://www.nhl.com/>
construct { ?game :awayHits ?hits }
{ select ?game (count (?hit) as ?hits)
 {?game a :Game . ?game :awayteam ?team .
  ?game :play ?hit . ?hit a :Hit . ?hit :team ?team
 }
 group by ?game ?team
}
")

(def hometeam-violent-pim "
prefix : <http://www.nhl.com/>
construct { ?game :homeViolentPim ?pim }
{ select ?game (sum (?minutes) as ?pim)
 {?game a :Game . ?game :hometeam ?team .
  ?game :play ?pen . ?pen a :ViolentPenalty .
  ?pen :team ?team . ?pen a / :penaltyMinutes ?minutes }
 group by ?game ?team
}
")

(def awayteam-violent-pim "
prefix : <http://www.nhl.com/>
construct { ?game :awayViolentPim ?pim }
{ select ?game (sum (?minutes) as ?pim)
 {?game a :Game . ?game :awayteam ?team .
  ?game :play ?pen . ?pen a :ViolentPenalty .
  ?pen :team ?team . ?pen a / :penaltyMinutes ?minutes }
 group by ?game ?team
}
")

(def hometeam-pim "
prefix : <http://www.nhl.com/>
construct { ?game :homePim ?pim }
{ select ?game (sum (?minutes) as ?pim)
 {?game a :Game . ?game :hometeam ?team .
  ?game :play ?pen . ?pen a :Penalty .
  ?pen :team ?team . ?pen a / :penaltyMinutes ?minutes }
 group by ?game ?team
}
")

(def awayteam-pim "
prefix : <http://www.nhl.com/>
construct { ?game :awayPim ?pim }
{ select ?game (sum (?minutes) as ?pim)
 {?game a :Game . ?game :awayteam ?team .
  ?game :play ?pen . ?pen a :Penalty .
  ?pen :team ?team . ?pen a / :penaltyMinutes ?minutes }
 group by ?game ?team
}
")

(def hometeam-shots "
prefix : <http://www.nhl.com/>
construct { ?game :homeShots ?shots }
{ select ?game (count (?shot) as ?shots)
 {?game a :Game . ?game :hometeam ?team .
  ?shot :team ?team . ?game :play ?shot . ?shot a :Shot }
 group by ?game ?team
}
")

(def awayteam-shots "
prefix : <http://www.nhl.com/>
construct { ?game :awayShots ?shots }
{ select ?game (count (?shot) as ?shots)
 {?game a :Game . ?game :awayteam ?team .
  ?shot :team ?team . ?game :play ?shot . ?shot a :Shot }
 group by ?game ?team
}
")

(def hometeam-fightPim "
prefix : <http://www.nhl.com/>
construct { ?game :homeFights ?fightPim }
{ select ?game (sum (?minutes) as ?fightPim)
 {?game a :Game . ?game :hometeam ?team .
  ?game :play ?fight . ?fight a :FightingPenalty .
  ?fight :team ?team . ?fight a / :penaltyMinutes ?minutes}
 group by ?game ?team
}
")

(def awayteam-fightPim "
prefix : <http://www.nhl.com/>
construct { ?game :awayFights ?fightPim }
{ select ?game (sum (?minutes) as ?fightPim)
 {?game a :Game . ?game :hometeam ?team .
  ?game :play ?fight . ?fight a :FightingPenalty .
  ?fight :team ?team . ?fight a / :penaltyMinutes ?minutes}
 group by ?game ?team
}
")

(def hometeam-goals "
prefix : <http://www.nhl.com/>
construct { ?game :homeGoals ?goals }
{ select ?game (count (?goal) as ?goals)
 {?game a :Game . ?game :hometeam ?team .
  ?game :play ?goal . ?goal a :Goal . ?goal :team ?team
  }
 group by ?game ?team
}
")

(def awayteam-goals "
prefix : <http://www.nhl.com/>
construct { ?game :awayGoals ?goals }
{ select ?game (count (?goal) as ?goals)
 {?game a :Game . ?game :awayteam ?team .
  ?game :play ?goal . ?goal a :Goal . ?goal :team ?team
  }
 group by ?game ?team
}
")

(defn build-model [m]
  (build   (pull team-names m)

           (pull hometeam-hits m)
           (pull hometeam-violent-pim m)
           (pull hometeam-pim m)
           (pull hometeam-shots m)
           (pull hometeam-fightPim m)
           (pull hometeam-goals m)
           
           (pull awayteam-hits m)
           (pull awayteam-violent-pim m)
           (pull awayteam-pim m)
           (pull awayteam-shots m)
           (pull awayteam-fightPim m)
           (pull awayteam-goals m)
           ))