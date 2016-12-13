(ns clj-4-the-brave.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(+ 2 3)

;; Maps
(defn error-message
  [severity]
  (str "OH whatever!!"
    (if (= severity :mild)
      "convinced"
      "doomed")))

(:a {:a "a" :b "b" :c "c"})

(:d {:a "a" :b "b" :c "c"} "Default Value")

;; Vectors
[0 1 2]

(get [0 1 2] 1)

(get [1 {"name" "value"} "string"] 2)

(vector "a" 1 2/3)

(conj [0 1 2 3] 4)

;; Lists
'(1 2)

;; Lists doesn't support get method instead use nth
(nth '(1 2 3) 1)

;; nth to retrieve an element from a list is slower than using get to retrieve an element from a vector. This is because Clojure has to traverse all n elements of a list to get to the nth, whereas it only takes a few hops at most to access a vector element by its index

;; list values can be heterogeneous similar to other data structures
(list "a" 0 {:a "a"})

;; conj function adds elements to the begining of the list

(conj '(1 2 3) 4)

;; Sets - A collection of unique values
;; 2 kinds of sets hash-sets and sorted-sets

#{1 "a" :a}

(hash-set 1 1 2 2)

(conj #{:a :b} :a)

(conj #{:a :b} :c)

(set [0 1 2])
(set '(0 1 2))

(set [0 0 1 1 1 2 2 2])
(set '(0 0 1 1 1 2 2 2))

(contains? #{"a" "b" "c"} "a")
(contains? #{"a" "b" "c"} "A")
(contains? #{nil} nil)

(:a #{:a :b})

(get #{:a :b} :a)
(get #{:a :b} :c)
;; The following is option of using get to test if the value is nil is not right. Its better to use contains? instead.
(get #{:a nil} nil)

(inc 1)

(map inc [1 2 3])

(+ 1 2 3)

(defn my-first
  [[first-thing]] ; Notice that first-thing is within a vector
  first-thing)

(my-first [0 1])

(defn anounce-treaseure-location
 [{lat :lat lng :lng}]
 (println "Treasure location lat: " lat)
 (println "Treasure location lng: " lng))

(defn anounce-treasure-location-2
  [{:keys [lat lng]}]
  (println "Treasure location lat: " lat)
  (println "Treasure location lng: " lng))

(defn receive-treasure-location
  [{:keys [lat lng] :as treasure-location}]
  (println "Treasure location lat: " lat)
  (println "Treasure location lng: " lng))


;;The basic idea behind destructuring is that it lets you concisely bind names to values within a collection.


((fn [x] (* x 3)) 8)

(map (fn [x] (str "hi " x)) ["A" "B"])

;; you could even assiociate an annonymous function with a name

(def my-multiplier (fn [x] (* x x)))

(my-multiplier 2)

;; Annonymous function #() where as set #{}
;; This strange-looking style of writing anonymous functions is made possible by a feature called reader macros.

(#(* % 3) 8)

(#(* % %) 8)

(def my-multiplier-2 #(* % %))

(my-multiplier-2 8)

(map #(str "hi " %) ["A" "B"])

;; If your anonymous function takes multiple arguments, you can distinguish them like this: %1, %2, %3, and so on. % is equivalent to %1

( #(str %1 "&" %2) "H" "M")

;; You can also pass a rest parameter with %&:

(#(identity %&) 1 "blarg" :yip)

;; Identity returns the argument it’s given without altering it

(defn inc-maker
  "Create a custom incrementor"
  [inc-by]
  #(+ % inc-by))


(def inc3 (inc-maker 3))

(inc3 7) ; =>10

;; let meaning let it be

(let [x 3] x)

(let [x 3 y 4] (+ x y))

;; scope of let

(def x 0)
(let [x 1] x)

(def dalmatian-list ["Pongo" "Pardita" "Puppy 1" "Puppy 2"])

(let [dalmatians (take 2 dalmatian-list)] dalmatians)

(let [[pongo & dalms] dalmatian-list]
  [pongo dalms])

(let [[pongo] dalmatian-list]
  [pongo])

(let [pongo dalmatian-list] 
  pongo)

;; Notice that the value of a let form is the last form in its body that is evaluated
;; let forms follow all the destructuring rules introduced in “Calling Functions” on page 48
;; let forms have two main uses. First, they provide clarity by allowing you to name things. Second, they allow you to evaluate an expression only once and reuse the result
;; This is “especially important when you need to reuse the result of an expensive function call, like a network API call. It’s also important when the expression has side effects
;; let creates local variables

(into [] (set [:a :a]))
 
(let [[part & remaining] remaining-asym-parts]
   (recur remaining
          (into final-body-parts
                (set [part (matching-part part)]))))

;; original expression instead of using let, it would be a mess! 
(recur (rest remaining-asym-parts)
       (into final-body-parts
             (set [(first remaining-asym-parts) (matching-part (first remaining-asym-parts))])))

;; loop
(loop [iteration 0]
  (println (str "Iteration " iteration))
  (if (> iteration 3)
    (println "Goodbye!")
    (recur (inc iteration))))

(loop [iter 1 acc  0]
  (println iter " " acc)
  (if (> iter 10)
      (println acc)
      (recur (inc iter) (+ acc iter))))


;; [iteration 0], begins the loop and introduces a binding with an iqnitial value
;; The above loop function can be accomplished with the following code
;; Multiple arity function

(defn recursive-printer
   ([]
    (recursive-printer 0)) ; 0 arity
   ([iteration]
    (println "Iteration " iteration)
    (if (> iteration 3)
       (println "Goodbye!")
       (recursive-printer (inc iteration))))) ; 1 arity

(recursive-printer) ;; call the function

;; loop is a bit more verbose. Also, loop has much better performance

;; Regular Expression
;; The literal notation for a regular expression is to place the expression in quotes after a hash mark

#"regular-expression"  ;; eg: #"^left-"

;; clojure.string/replace uses the regular expression #"^left-" to match strings starting with "left-" in order to replace "left-" with "right-". The carat, ^, is how the regular expression signals that it will match the text "left-" only if it’s at the beginning of the string

;;You can test this with re-find, which checks whether a string matches the pattern described by a regular expression, returning the matched text or nil if there is no match

(re-find #"^left-" "left-eye")

(re-find #"^left-" "eye-eye")

(defn matching-part
   [part]
   {:name (clojure.string/replace (:name part) #"^left-" "right-")
    :size (:size part)})

(matching-part {:name "left-eye" :size 1})
(matching-part {:name "eye" :size 1})

;; reduce  (reduce + [1 2 3 4]) => 10

(reduce + [1 2 3 4])

;; reduce syntax with optional initial value (reduce + 15 [1 2 3 4]) => 25

(reduce + 15 [1 2 3 4])

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])


(defn matching-part
   [part]
   {:name (clojure.string/replace (:name part) #"^left-" "right-")
      :size (:size part)})


(defn symmetrize-body-parts
    "Expects a seq of maps that have a :name and :size"
    [asym-body-parts]
    (loop [remaining-asym-parts asym-body-parts
           final-body-parts []]
      (if (empty? remaining-asym-parts)
        final-body-parts
        (let [[part & remaining] remaining-asym-parts]
          (recur remaining
           (into final-body-parts
                  (set [part (matching-part part)])))))))

(symmetrize-body-parts asym-hobbit-body-parts)

(def symmetrized-output [{:name "head", :size 3} 
                         {:name "left-eye", :size 1} {:name "right-eye", :size 1} 
                         {:name "left-ear", :size 1} {:name "right-ear", :size 1} 
                         {:name "mouth", :size 1} 
                         {:name "nose", :size 1} 
                         {:name "neck", :size 2} 
                         {:name "left-shoulder", :size 3} {:name "right-shoulder", :size 3} 
                         {:name "right-upper-arm", :size 3} {:name "left-upper-arm", :size 3} 
                         {:name "chest", :size 10} 
                         {:name "back", :size 10} 
                         {:name "left-forearm", :size 3} {:name "right-forearm", :size 3} 
                         {:name "abdomen", :size 6} 
                         {:name "left-kidney", :size 1} {:name "right-kidney", :size 1} 
                         {:name "left-hand", :size 2} {:name "right-hand", :size 2} 
                         {:name "right-knee", :size 2} {:name "left-knee", :size 2} 
                         {:name "right-thigh", :size 4} {:name "left-thigh", :size 4} 
                         {:name "right-lower-leg", :size 3} {:name "left-lower-leg", :size 3} 
                         {:name "right-achilles", :size 1} {:name "left-achilles", :size 1} 
                         {:name "right-foot", :size 2} {:name "left-foot", :size 2}])


(defn my-reduce
  ([f initial coll]
   (loop [result initial remaining coll]
     (if(empty? remaining)
      result
      (recur (f result (first remaining)) (rest remaining)))))
  ([f [head & tail]]
    (my-reduce f head tail)))


;; reduce has 2 syntax structures (reduce f coll) (reduce f val coll)

;; if I use the first syntax by removing [] in the following function better-symmetrize-body-parts, it returs {:name "right-eye", :size 1} the last element of the result

;; if I use the second syntax it returns the expected result [{:name "head", :size 3} {:name "left-eye", :size 1} {:name "right-eye", :size 1}]

;; the return value of the function better-symmetrize-body-parts is final-body-parts of into function not asym-body-parts, which is just passing input values to reduce function

(defn better-symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
              (into final-body-parts (set [part (matching-part part)])))
          []
          asym-body-parts))

(better-symmetrize-body-parts asym-hobbit-body-parts)

(better-symmetrize-body-parts 
 (vector (nth asym-hobbit-body-parts 0) (nth asym-hobbit-body-parts 1)))


(vector (nth asym-hobbit-body-parts 0) (nth asym-hobbit-body-parts 1))


;; reduce is more expressive over using loop, by abstracting the reduce process into a function that takes another function as an argument, your program becomes more composable


(defn hit
  [asym-body-parts]
  (let [sym-parts (better-symmetrize-body-parts asym-body-parts)
        body-part-size-sum (reduce + (map :size sym-parts))
        target (rand body-part-size-sum)]
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining (+ accumulated-size (:size (first remaining))))))))


(hit asym-hobbit-body-parts)


