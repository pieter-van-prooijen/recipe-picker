(ns recipe-picker.views
  (:require
   [re-frame.core :as re-frame]
   [recipe-picker.events :as events]
   [recipe-picker.db :as db]))

;; Shortcuts from the re-frame documentation
(def <sub (comp deref re-frame.core/subscribe))
(def >evt re-frame.core/dispatch)

;; Date display
(defn show-iso8601 [dt]
  "not used"
  #_(tf/unparse (:date-time tf/formatters) dt)) ;; in the default timezone

(defn show-days-ago [dt]
  "not used"
  #_(tc/in-days (tc/interval dt (tc/now))))

(defn pure-tag-selector [label selected all-values select-fn]
  [:div.field
   [:label.label label]
   [:div.control
    [:div.select
                                        ; handle changes on the select, not the individual options
     [:select {:value selected
               :on-change select-fn}
      (map (fn [v]   ; *not* mapv, won't expand inline otherwise
             ^{:key v} [:option v])
           all-values)]]]])

(defn tag-selector [tag label]
  (pure-tag-selector label
                     (<sub [::db/selected-tag-value tag])
                     (<sub [::db/all-tag-values tag])
                     (fn [e]
                       (.preventDefault e)
                       (>evt [::events/select-tag tag (.. e -target -value)]))))

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
     [:span.tag.is-large
      (str (count (<sub [::db/filtered-recipes])) " recipes")]
     [:span.tag.is-large {:title (show-iso8601 (<sub [::db/last-modified]))}
      (str (show-days-ago (<sub [::db/last-modified])) " days ago")]]]
   [recipe-list (<sub [::db/filtered-recipes])]])
