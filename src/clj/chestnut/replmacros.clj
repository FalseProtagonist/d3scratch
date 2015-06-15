(ns replmacros)
(defmacro make-config [& forms]
  `(do ~@(for [[name body] (partition 2 forms)]
           `(def ~name ~body))))


(defmacro make-config [& forms]
  `(do ~@(for [[name body] (partition 2 forms)]
           `(def ~name ~body))))


(defmacro deflet [& mydefs]
  '(do ~@(for [[name body] (partition 2 mydefs)]
           '(def ~name ~body))))

(let [[& x] [2 4]]
  (print x))
(defn testr [[& a] & b]
  (for [[a0 b0] (partition 2 a)])
  (print "hello"))
