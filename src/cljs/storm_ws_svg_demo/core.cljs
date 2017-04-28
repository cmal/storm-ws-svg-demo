(ns storm-ws-svg-demo.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [re-frisk.core :refer [enable-re-frisk!]]
   [storm-ws-svg-demo.events]
   [storm-ws-svg-demo.subs]
   [storm-ws-svg-demo.views :as views]
   [storm-ws-svg-demo.config :as config]
   [storm-ws-svg-demo.client :as client]
   )
  )

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (enable-re-frisk!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
