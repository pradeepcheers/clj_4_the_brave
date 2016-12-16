(ns clj-4-the-brave.core)

;; Clojure defines map and reduce functions in terms of the sequence abstraction but not in terms of specific data structure.

;; As long as a data structure responds to the core sequence operations (thr functions first, rest and cons) it will work with map, reduce, and oodles of other sequence functions for free. This is what clojurists mean by programming to abstractions

;; If the core sequence functions first, rest, and cons work on a data structure, you can say the data structure implements the sequence abstraction. Lists, vectors, sets, and maps all implement the sequence abstraction, so they all work with map


;; Map function

(map (fn [x] (str "Hi " x)) ["A" "B"])
(map #(str "Hi " %) ["A" "B"])

(map #(str "Hi " %) '("A" "B"))

(map #(str "Hi " %) #{"A" "B"})

(map #(str "Hi " (second %)) {:key "map value"})


;; map can work with unsorted sets what about sorted sets??

;; The point is to appreciate the distinction between the seq abstraction in Clojure and the concrete implementation of a linked list. It doesn’t matter how a particular data structure is implemented

;; when it comes to using seq functions on a data structure, all Clojure asks is “can I first, rest, and cons it?” If the answer is yes, you can use the seq library with that data structure


;; Reduce

(reduce (fn [new-map [key val]]
          (assoc new-map key (inc val)))
{}
{:max 30 :min 10})

(reduce (fn [new-map [key val]]
          (if (> val 4)
            (assoc new-map key val)
            new-map))
{}
{:human 4.1 :critter 3.9})

;; take, drop take-while and drop-while

(take 2 [0 1 2])
(take 2 '(0 1 2))

(drop 2 [0 1 2])
(drop 2 '(0 1 2))

(def food-journal
  [{:month 1 :day 1 :human 5.3 :critter 2.3}
   {:month 1 :day 2 :human 5.1 :critter 2.0}
   {:month 2 :day 1 :human 4.9 :critter 2.1}
   {:month 2 :day 2 :human 5.0 :critter 2.5}
   {:month 3 :day 1 :human 4.2 :critter 3.3}
   {:month 3 :day 2 :human 4.0 :critter 3.8}
   {:month 4 :day 1 :human 3.7 :critter 3.9}
   {:month 4 :day 2 :human 3.7 :critter 3.6}])

(take-while #(< (:month %) 3) food-journal)




