(ns storm-ws-svg-demo.events
  (:require
   [re-frame.core :as re-frame]
   [storm-ws-svg-demo.db :as db]
   )
  )


(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
 :loc-change
 (fn [db [_ [id x y]]]
   (let [q (get-in db [:tags id])]
     (if (nil? q)
       (assoc-in  db [:tags id] #queue [[x y]])
       (if (= 10 (count q))
         (update-in db [:tags id] #(conj (pop %) [x y]))
         (update-in db [:tags id] #(conj % [x y])))))))
