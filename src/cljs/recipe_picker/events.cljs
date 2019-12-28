(ns recipe-picker.events
  (:require
   [re-frame.core :as re-frame]
   [recipe-picker.db :as db]
   [recipe-picker.csv :as csv]
   [recipe-picker.config :refer [debug?]]
   [goog.net.XhrIo :as xhrio]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

;; http effect
(re-frame/reg-fx
 :http
 (fn [[url success-event]]
   (xhrio/send url (fn [ev]
                     (let [response (.. ev -target getResponseText)]
                       (re-frame/dispatch [success-event response]))))))

(re-frame/reg-event-fx
 ::fetch-recipes
 (fn-traced [_ [_ url]]            
            {:http [url ::recipes-fetched]
             :dispatch [::reset-tags]}))

(re-frame/reg-event-db
 ::recipes-fetched
 (fn-traced [db [_ csv]]
            (when debug?
              (println "fetched recipes " (csv/to-recipes csv)))
            (-> db
                (assoc :all-recipes (csv/to-recipes csv))
                (assoc :shuffle 0))))

(re-frame/reg-event-db
 ::select-tag
 (fn-traced [db [_ tag value]]
            (let [db (assoc db :shuffle 0)] ; any tag selection will disable the shuffle
              (if (= value db/wild-card-value)
                (update-in db [:query] dissoc tag) ; there's no dissoc-in
                (assoc-in db [:query tag] value))))) 

(re-frame/reg-event-db
 ::reset-tags
 (fn-traced [db _]
            (-> db
                (assoc :query {})
                (assoc :shuffle 0))))

(re-frame/reg-event-db
 ::shuffle
 (fn-traced [db _]
            (update-in db [:shuffle] inc)))
