(ns recipe-picker.core
  (:require
   [reagent.dom]
   [re-frame.core :as re-frame]
   [recipe-picker.events :as events]
   [recipe-picker.views :as views]
   [recipe-picker.config :as config]))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent.dom/render [views/main-panel]
                      (.getElementById js/document "app")))

;; invoked by builds/app/modules/app/init-fn in shadow-cljs.edn
(defn init []
  (re-frame/dispatch-sync [::events/fetch-recipes "data/recipes.csv"])
  (dev-setup)
  (mount-root))

;; not needed for shadow-cljs
(defonce did-init? false)
(when-not did-init? (init))
