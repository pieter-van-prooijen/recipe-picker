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
   (xhrio/send url (fn [^js/Event ev]
                     (let [response (.. ev -target getResponseText)
                           last-modified (.getResponseHeader (.-target ev) "Last-Modified")]
                       (re-frame/dispatch [success-event [response last-modified]]))))))

(re-frame/reg-event-fx
 ::fetch-recipes
 (fn-traced [_ [_ url]]            
            {:http [url ::recipes-fetched]
             :dispatch [::reset-tags]}))

(re-frame/reg-event-db
 ::recipes-fetched
 (fn-traced [db [_ [csv last-modified]]]
            (when debug?
              (println "fetched recipes " #_(csv/to-recipes csv)))
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

(comment

  (Date/parse "Thu, 09 Dec 2021 11:59:33 GMT")
  (->>  "Thu, 09 Dec 2021 11:59:33 GMT"
        (goog.date.DateTime/fromRfc822String)
        #_(time-core/minute)
        #_(time-format/unparse (:date-time time-format/formatters)))

  (time-format/show-formatters)
  (time-core/default-time-zone)
  (time-format/parse (time-format/formatters :rfc822) "Thu, 09 Dec 2021 11:59:33 GMT")
  (time-format/parse  "Thu, 09 Dec 2021 11:59:33 GMT")
  ,)
