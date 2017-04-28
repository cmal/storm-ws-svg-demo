(ns storm-ws-svg-demo.client
  (:require
   [cljs.core.async :as async :refer (<! >! put! chan)]
   [taoensso.encore :as encore :refer-macros (have have?)]
   [taoensso.timbre :as timbre]
   [taoensso.sente  :as sente :refer (cb-success?)]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]
   [cljs.reader :refer [read-string]]
   )
  (:require-macros
   [cljs.core.async.macros :as asyncm :refer (go go-loop)]
   [storm-ws-svg-demo.core :refer [log]]
   )
)

(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket!
       "/chsk"
       {:protocol "http:" :host "0.0.0.0:19009" :type :ws :packer :edn})]
  (def chsk       chsk)
  (def ch-chsk    ch-recv) ; ChannelSocket's receive channel
  (def chsk-send! send-fn) ; ChannelSocket's send API fn
  (def chsk-state state)   ; Watchable, read-only atom
  )

(defmulti push-evt-handler
  "handle :chsk/recv type event"
  first
  )

(defmethod push-evt-handler :node/loc
  [msg]
  (let [[evt-type s] msg
        [id x y] (read-string s)]
    (dispatch [:loc-change [id x y]])))

(defmethod push-evt-handler :default
  [msg]
  (log "other type of msg:" msg))


(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id ; Dispatch on event-id
  )

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg :keys [id ?data event]}]
  (-event-msg-handler ev-msg))

(defmethod -event-msg-handler
  :default ; Default/fallback case (no other matching handler)
  [{:as ev-msg :keys [event]}]
  (log "Unhandled event: %s" event))

(defmethod -event-msg-handler :chsk/state
  [{:as ev-msg :keys [?data]}]
  (let [[old-state-map new-state-map] (have vector? ?data)]
    (if (:first-open? new-state-map)
      (log "Channel socket successfully established!: %s" new-state-map)
      (log "Channel socket state change: %s"              new-state-map))))

(defmethod -event-msg-handler :chsk/recv
  [{:as ev-msg :keys [?data]}]
  #_(log "Push event from server: %s" ?data)
  (push-evt-handler ?data))

(defmethod -event-msg-handler :chsk/handshake
  [{:as ev-msg :keys [?data]}]
  (let [[?uid ?csrf-token ?handshake-data] ?data]
    (log "Handshake: %s" ?data)))

;; TODO Add your (defmethod -event-msg-handler <event-id> [ev-msg] <body>)s here...

;;;; Sente event router (our `event-msg-handler` loop)

;;;; Sente event handlers

(defonce router_ (atom nil))
(defn  stop-router! [] (when-let [stop-f @router_] (stop-f)))
(defn start-router! []
  (stop-router!)
  (reset! router_
    (sente/start-client-chsk-router!
     ch-chsk event-msg-handler))
  (log "router restarted!"))

(defn start! [] (start-router!))

(defonce _start-once (start!))
