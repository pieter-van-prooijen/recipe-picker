(ns recipe-picker.views
  (:require
   [re-frame.core :as re-frame]
   [recipe-picker.events :as events]
   [recipe-picker.db :as db]))

;; Shortcuts from the re-frame documentation
(def <sub (comp deref re-frame.core/subscribe))
(def >evt re-frame.core/dispatch)

(defn tag-selector [tag label]
  [:div.field
   [:label.label label]
   [:div.control
    [:div.select
                                        ; handle changes on the select, not the individual options
     [:select {:value (<sub [::db/selected-tag-value tag])
               :on-change (fn [e]
                            (.preventDefault e)
                            (>evt [::events/select-tag tag (.. e -target -value)]))}
      (let [all-values (<sub [::db/all-tag-values tag])]
        (map (fn [v]   ; *not* mapv, won't expand inline otherwise
               ^{:key v} [:option v])
             all-values))]]]])

(defn recipe-list [recipes]
  [:table.table
   [:thead
    [:tr
     [:td "Name"]
     [:td "Location"]]]
   [:tbody
    (map (fn [recipe]
           ^{:key (:title recipe)}
           [:tr
            [:td.has-text-weight-bold (:title recipe)]
            [:td (:location recipe)]])
         recipes)]])

(defn main-panel []
  [:div.container
   [:h1.title "Recipe Picker"]
   [:form
    [tag-selector :carbs "Carbs"]
    [tag-selector :vegs "Vegs"]
    [tag-selector :misc "Misc"]
    [:div.control
     [:button.button {:on-click (fn [e]
                                  (.preventDefault e)
                                  (>evt [::events/reset-tags]))} "Reset"]
     [:button.button {:on-click (fn [e]
                                  (.preventDefault e)
                                  (>evt [::events/shuffle]))} "Shuffle"]
     [:span.tag.is-large (count (<sub [::db/filtered-recipes]))]]]
   [recipe-list (<sub [::db/filtered-recipes])]])
