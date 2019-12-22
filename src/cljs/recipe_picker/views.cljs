(ns recipe-picker.views
  (:require
   [re-frame.core :as re-frame]
   [recipe-picker.events :as events]
   [recipe-picker.db :as db]))


(def <sub (comp deref re-frame.core/subscribe))   ;; aka listen (above)
(def >evt re-frame.core/dispatch)

(defn tag-option [tag value]
  [:option
   {:on-click (fn [_] (>evt [::events/select-tag tag value]))}
   value])

(defn tag-selector [tag label]
  [:div.field
   [:label.label label]
   [:div.control
    [:div.select
     [:select
      (map (fn [v] ^{:key v} [tag-option tag v]) (<sub [::db/all-tag-values tag]))]]]])  ; *not* mapv, won't expand inline otherwise

(defn main-panel []
  [:div.level
   [:div.level-item
    [:div
     [:h1.title "Recipe Picker"]
     [:form
      [tag-selector :carbs "Carbs"]
      [tag-selector :vegs "Vegs"]
      [tag-selector :misc "Misc"]]]]])
