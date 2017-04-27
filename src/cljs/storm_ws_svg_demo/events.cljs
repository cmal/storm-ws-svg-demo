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
