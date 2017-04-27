(ns storm-ws-svg-demo.views
    (:require [re-frame.core :refer [subscribe dispatch]]))

(defn tag
  [t]
  [:circle
   {:id (key t)
    :cx (first (val t))
    :cy (second (val t))
    :r 2
    :style {:stroke "#600" :fill "#600"}}])

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
       [anchor a])
     (for [t tags]
       [tag t])]))

(defn main-panel []
  (let [name (subscribe [:name])]
    (fn []
      [:div
       [:div "Hello from " @name]
       [:div "Hello again"]
       [svg-panel]])))
