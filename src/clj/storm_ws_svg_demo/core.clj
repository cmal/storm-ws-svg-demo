(ns storm-ws-svg-demo.core)

(defmacro log [& args]
  `(do (.log js/console ~@args) nil))
