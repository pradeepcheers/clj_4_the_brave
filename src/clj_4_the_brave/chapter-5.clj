(ns clj-4-the-brave.core
  (:require [clojure.string :as s]))

;; how disentangling data and functions gives you more power and flexibility why it’s powerful to program to a small set of data abstractions

;; Except for println and rand, all the functions you’ve used up till now have been pure functions. What makes them pure functions, and why does it matter? A function is pure if it meets two qualifications:

;;It always returns the same result if given the same arguments. This is called referential transparency, and you can add it to your list of $5 programming terms.

;; It can’t cause any side effects. That is, the function can’t make any changes that are observable outside the function itself—for example, by changing an externally accessible mutable object or writing to a file.

;; Pure functions are completely isolated, unable to impact other parts of your system. When you use them, you don’t have to ask yourself, “What could I break by calling this function?” They’re also consistent: you’ll never need to figure out why passing a function the same arguments results in different return values, because that will never happen. Pure functions are as stable and problem free as arithmetic

;; Pure functions are Referential Transparent
;; Returns the same result when called with the same arguments

(defn wisdom 
  [words]
  (str words ", Daniel-san"))

(wisdom "Always bathe every morning")

;; By contrast the following function is not pure. Since it is not referential transparent

(defn year-end-evaluation
  []
  (if (> (rand) 0.5)
    "You get a raise!"
    "Better luck next year!"))

(year-end-evaluation)

;; If a function reads from a file, it is not referential transparent because the file content can change.

;; The following functions analysis-file is referential transparent but the function analysis is

(defn analyse-file
  [filename]
  (analysis (slurp filename)))

(defn analysis
  [text]
  (str "Character count: " (count text)))

;; When using a referentially transparent function, you never have to consider what possible external conditions could affect the return value of the function. This is especially important if your function is used multiple places or if it is nested deeply in a chain of function calls. In both cases the changes to external conditions won't cause your code to break.

;; Pure funtions have no side effects, Clojure makes your job easier by going to great lengths to limit the side effects. All of its core data structres are immutable. “How can you do anything without side effects? Well, that’s what the next section is all about!

;; Living with Immutable Data Structure

;; Immutable data structures ensures that your code has no side effects, but how do you get anything done without side effects?

;; Recursion instead of for or while

(def great-baby-name "Rosanthony")

great-baby-name

(let [great-baby-name "Bloodthunder"]
  great-baby-name)

great-baby-name

;; In this example, you first bind the name great-baby-name to "Rosanthony" within the global scope. Next, you introduce a new scope with let. Within that scope, you bind great-baby-name to "Bloodthunder". Once Clojure finishes evaluating the let expression, you’re back in the global scope, and great-baby-name evaluates to "Rosanthony" once again.

;; Clojure lets you work around this apparent limitation with recursion. The following example shows the general approach to recursive problem solving:

(defn sum 
  ([vals] (sum vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (sum (rest vals) (+ (first vals) accumulating-total)))))

;; “Like all recursive solutions, this function checks the argument it’s processing against a base condition. In this case, we check whether vals is empty at ➋. If it is, we know that we’ve processed all the elements in the collection, so we return accumulating-total. If vals isn’t empty, it means we’re still working our way through the sequence, so we recursively call sum passing it two arguments: the tail of vals with (rest vals) and the sum of the first element of vals plus the accumulating total with (+ (first vals) accumulating-total). In this way, we build up accumulating-total and at the same time reduce vals until it reaches the base case of an empty collection

(sum [39 5 1]) ;; single-arity body calls two arity body

(sum [39 5 1] 0)

(sum [5 1] 39)

(sum [1] 44)

(sum [] 45) ;; base case is reached, so return accoumplishing-total

;; Each recursive call to sum creates a new scope where vals and accoumplishing-total are bound to different values, all without needing to alter the values originally passed to the function or perform any internal mutation. As you can see you can get along fine with out mutation. 

;; Note that you should generally use recur when doing recursion for performance reasons. The reason is that clojure doesn't provide tail call optimization

(defn sum
  ([vals] (sum vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (recur (rest vals) (+ (first vals) accumulating-total)))))

;; Using recur isn’t that important if you’re recursively operating on a small collection, but if your collection contains thousands or millions values, you will definitely need to whip out recur so you don’t blow up your program with a stack overflow. One last thing! You might be saying, “Wait a minute—what if I end up creating thousands of intermediate values? Doesn’t this cause the program to thrash because of garbage collection or whatever? Very good question, eagle-eyed reader! The answer is no! The reason is that, behind the scenes, Clojure’s immutable data structures are implemented using structural sharing, which is totally beyond the scope of this book. It’s kind of like Git! Read this great article if you want to know more: http://hypirion.com/musings/understanding-persistent-vector-pt-1

;; Function Composition instead of Attrubute Mutation

;; Another way you might be used to using mutation is to build up the final state of an object.

;;(require '[clojure.string :as s])


(defn clean
  [text]
  (s/replace (s/trim text) #"lol" "LOL"))

(clean "My boa constrictor is so sassy lol!  ")


;; Combining functions like this—so that the return value of one function is passed as an argument to another—is called function composition. In fact, this isn’t so different from the previous example, which used recursion, because recursion continually passes the result of a function to another function; it just happens to be the same function. In general, functional programming encourages you to build more complex functions by combining simpler functions.

;; Going beyond immediately practical concerns, the differences between the way you write object-oriented and functional code point to a deeper difference between the two mindsets. Programming is about manipulating data for your own nefarious purposes (as much as you can say it’s about anything). In OOP, you think about data as something you can embody in an object, and you poke and prod it until it looks right. During this process, your original data is lost forever unless you’re very careful about preserving it. By contrast, in functional programming you think of data as unchanging, and you derive new data from existing data. During this process, the original data remains safe and sound. In the preceding Clojure example, the original caption doesn’t get modified. It’s safe in the same way that numbers are safe when you add them together; you don’t somehow transform 4 into 7 when you add 3 to it.

;; Cool Things to Do with Pure Functions

;; you can derive new functions from the existing functions in the same way that you derive new data from existing data. You've already seen one function, partial, that creates new functions. This section introduces you to two more functions, comp and memoize, which rely on referential transparency, immutability, or both.

;; comp is used for creating a new function from the composition of any number of functions.

((comp inc *) 2 3)

;; Here, you create an anonymous function by composing the inc and * functions. Then, you immediately apply this function to the arguments 2 and 3. The function multiplies the numbers 2 and 3 and then increments the result. Using math notation, you’d say that, in general, using comp on the functions f1, f2, ... fn, creates a new function g such that g(x1, x2, ... xn) equals f1(f2(fn(x1, x2, ... xn))). One detail to note here is that the first function applied—* in the code shown here—can take any number of arguments, whereas the remaining functions must be able to take only one argument. Here’s an example that shows how you could use comp to retrieve character attributes in role-playing games:

(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})

(def c-int (comp :intelligence :attributes))

(def c-str (comp :strength :attributes))

(def c-dex (comp :dexterity :attributes))

(c-int character)

(c-str character)

(c-dex character)

;; In this example, you created three functions that help you look up a character’s attributes. Instead of using comp, you could just have written something like this for each attribute:

(fn [c] (:strength (:attributes c)))

;; But comp is more elegant because it uses less code to convey more meaning. When you see comp, you see immediately know that the resutling function's purpose is to combine existing functions in a well known way.

(defn spell-slots
  [char]
  (int (inc (/ (c-int char) 2))))


(spell-slots character)

;; First, you divide intelligence by two, then you add one, and then you use the int function to round down. Here’s how you could do the same thing with comp:

(def spell-slots-comp (comp int inc #(/ % 2) c-int))

(spell-slots-comp character)

;; To divide by two, all you needed to do was wrap the division in an anonymous function.

(defn two-comp
  [f g]
  (fn [& args]
    (f (apply g args))))

;; memoize, another cool function that allows you to memoize them so that Clojure remembers the result of a particular function call. You can do this because as you learned earlier, pure funtions are referentially transparent + is referentially transparent. You can replace (+ 3 (+ 5 8)) with (+ 3 13) or 16 and the program will have the same behaviour

;; Memoization lets you take advantage of referential transparency by storing the arguents passed to the function and the return value of the function. That way subsequent calls to the function with the same arguments can return the result immediately. This is especially useful for functions that take a lot of time to run. For example,

(defn sleepy-identity
  "Returns the given value after 1 second"
  [x]
  (Thread/sleep 1000)
  x)

(sleepy-identity "Mr. Fantastico") ;; after 1 sec

(sleepy-identity "Mr. Fantastico") ;; after 1 sec

;; memoize version

(def memoize-sleepy-identity (memoize sleepy-identity))

(memoize-sleepy-identity "Mr. Fantastico") ;; after 1 sec

(memoize-sleepy-identity "Mr. Fantastico") ;; returns immediately

;; Peg Thing




