(ns storm-ws-svg-demo.views
  (:require [re-frame.core :refer [subscribe dispatch]])
  (:require-macros
   [storm-ws-svg-demo.core :refer [log]]
   ))

(defn tag
  [t]
  (let [id (key t)
        vpos (vec (val t))
        cpos (last vpos)
        [cx cy] cpos
        cnt (count vpos)]
    [:g
     [:circle
      {:id id
       :cx cx
       :cy cy
       :r 2
       :style {:stroke "#600" :fill "#600"}}]
     (for [i (range (dec cnt))]
       (let [color (str "#" (apply str (repeat 2 (str (- 9 i)))) "0")]
         ^{:key i}
         [:line
          {:x1 (get-in vpos [i 0])
           :y1 (get-in vpos [i 1])
           :x2 (get-in vpos [(inc i) 0])
           :y2 (get-in vpos [(inc i) 1])
           :style {:stroke color
                   :stroke-width 2}}]))]))

(defn anchor
  [a]
  [:rect
   {:id (key a)
    :x (first (val a))
    :y (second (val a))
    :height 8
    :width 8
    :style {:stroke "#006" :fill "#006"}}])

(defn svg-panel
  []
  (let [tags @(subscribe [:tags])
        anchors @(subscribe [:anchors])]
    [:svg {:x 0 :y 0 :width 640 :height 480 :style {:border "1px solid #999"}}
     (for [a anchors]
       ^{:key (key a)}
       [anchor a])
     (for [t tags]
       ^{:key (key t)}
       [tag t])]))

(defn main-panel []
  (let [name (subscribe [:name])]
    (fn []
      [:div
       [:div "Hello from " @name]
       [:div "Hello again"]
       [svg-panel]])))
