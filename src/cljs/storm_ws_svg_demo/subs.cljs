(ns storm-ws-svg-demo.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :name
 (fn [db]
   (:name db)))

(reg-sub
 :tags
 (fn [db]
   (:tags db)))

(reg-sub
 :anchors
 (fn [db]
   (:anchors db)))
