(ns clj-4-the-brave.core)

;; how disentangling data and functions gives you more power and flexibility why it’s powerful to program to a small set of data abstractions

;; Except for println and rand, all the functions you’ve used up till now have been pure functions. What makes them pure functions, and why does it matter? A function is pure if it meets two qualifications:

;;It always returns the same result if given the same arguments. This is called referential transparency, and you can add it to your list of $5 programming terms.

;; It can’t cause any side effects. That is, the function can’t make any changes that are observable outside the function itself—for example, by changing an externally accessible mutable object or writing to a file.

;; Pure functions are completely isolated, unable to impact other parts of your system. When you use them, you don’t have to ask yourself, “What could I break by calling this function?” They’re also consistent: you’ll never need to figure out why passing a function the same arguments results in different return values, because that will never happen. Pure functions are as stable and problem free as arithmetic

;; Pure functions are Referential Transparent

