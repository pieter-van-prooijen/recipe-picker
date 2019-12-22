(ns recipe-picker.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [recipe-picker.events :as events]
   [recipe-picker.views :as views]
   [recipe-picker.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

;; invoked by builds/app/modules/app/init-fn in shadow-cljs.edn
(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
