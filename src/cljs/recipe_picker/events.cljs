(ns recipe-picker.events
  (:require
   [re-frame.core :as re-frame]
   [recipe-picker.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))

(re-frame/reg-event-db
 ::select-tag
 (fn-traced [db [_ tag value]]
            (assoc-in db [:query tag] value)))
