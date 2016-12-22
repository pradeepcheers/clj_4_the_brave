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


(map str ["a" "b" "c"] ["A" "B" "C"])

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

(drop-while #(< (:month %) 3) food-journal)

;; using take-while and drop-while together, you can get data for just Feb and Mar

(take-while #(< (:month %) 4) (drop-while #(< (:month %) 2) food-journal))

;; The above example uses drop-while to get rid of the Jan entries, and then it uses take-while on the result to keep taking entries until it reaches the first April entry.

;; filter and some: Use filter to return all elements of a sequence that test true for a predicate function. Here are the journal entries where human consumption is less than five liters:

(filter #(< (:human %) 5) food-journal)

;; filter endup processing all of the data. food-journal is already sorted by date, take while returns the data with out having to examine all of the data and hence it is efficient

;; some examines a collection containing any value that test true for a predicate. It return first truthy value (i.e; any value that's not false or nil)

(some #(> (:critter %) 5) food-journal)
(some #(> (:critter %) 3) food-journal)

;; To get / return the entry from the above example

(some #(and (> (:critter %) 3) %) food-journal)

(some #(and (> (:critter %) 3) (str "hello")) food-journal)

;; sort, sorts elements in ascending order

(sort [3 1 2])

;; if the soring needs to be more complicated, you can use sort-by, which allows you to apply a function (sometimes called a key function) to the elements of a sequence and uses the return values to determine the sort order.

(sort-by count ["aaa" "c" "bb"])

(sort ["aaa" "c" "bb"])  ;; resulting in elements sorted in alphabetical order.

;; concat, appends the members of one sequence to the end of another.

(concat [1 2] [3 "a"])


;; Lazy Seqs, a seq whose members aren't computed until you try to acess them. Computing a seq is called realizing the seq. Defering the computation until the moment its needed makes your programs more efficient. It has the surprising benefit pf allowing you to construct infinite sequence.

(def vampire-database
  {0 {:makes-blood-puns? false, :has-pulse? true  :name "McFishwich"}
   1 {:makes-blood-puns? false, :has-pulse? true  :name "McMackson"}
   2 {:makes-blood-puns? true,  :has-pulse? false :name "Damon Salvatore"}
   3 {:makes-blood-puns? true,  :has-pulse? true  :name "Mickey Mouse"}})


(defn vampire-related-details
  [social-security-number]
  (Thread/sleep 1000)
  (get vampire-database social-security-number))

  (vampire-related-details 0)
  (time (vampire-related-details 0))

(defn vampire?
  [record]
  (and (:makes-blood-puns? record)
       (not (:has-pulse? record))
       record))

  (vampire? {:makes-blood-puns? true :has-pulse? true :name "Mickey Mouse"})

(defn identify-vampire
  [social-security-numbers]
  (first (filter vampire?
                 (map vampire-related-details social-security-numbers))))

  (identify-vampire 1)

;; map is lazy













