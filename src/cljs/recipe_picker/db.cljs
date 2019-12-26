(ns recipe-picker.db
  (:require [re-frame.core :as re-frame :refer [reg-sub subscribe]]))

(def wild-card-value "*")

(def default-db
  {:all-recipes [{:title "recipe 1"
                  :location "book 1"
                  :tags {:carbs "Pasta"
                         :vegs "Erwten"
                         :misc "Anchovis"}}
                 {:title "recipe 2"
                  :location "book 2"
                  :tags {:carbs "Rijst"
                         :vegs "Paprika"
                         :misc "Ananas"}}
                 ]
   :query {}})

(reg-sub ::all-recipes
         (fn [db _]
           (:all-recipes db)))

;; answer all values for the given tag
(reg-sub ::all-tag-values
         (fn [_ _]
           (subscribe [::all-recipes]))
         (fn [all-recipes [_ tag]]
           (reduce (fn [r recipe]
                     (if-let [tag-value (get-in recipe [:tags tag])]
                       (conj r tag-value)
                       r))
                   #{wild-card-value}
                   all-recipes)))

(reg-sub ::query
         (fn [db _]
           (:query db)))

;; return the selected tag value or "*" if the tag was not selected
(reg-sub ::selected-tag-value
         (fn [_ _]
           (subscribe [::query]))
         (fn [query [_ tag]]
           (get query tag wild-card-value)))

(defn match-recipe [query recipe]
  (let [tags (:tags recipe)
        selected-tags (select-keys tags (keys query))]
    (= selected-tags query)))

(defn search [query recipes]
  (shuffle (filter (partial match-recipe query) recipes)))

(reg-sub ::filtered-recipes
         (fn [_ _]
           [(subscribe [::all-recipes])
            (subscribe [::query])])
         (fn [[all-recipes query] _]
           (search query all-recipes)))

(comment
  (def r1 {:title "recipe-1"
           :location "loc-1"
           :tags {:carbs :pasta
                  :veg :spinazie}})
  (def r2 {:title "recipe-2"
           :location "loc-2"
           :tags {:carbs :rijst
                  :veg :bloemkool}})

  (def recipes [r1 r2])
  (keys {:carbs :pasta})
  (select-keys (:tags r1) [:carbs])
  (match-recipe {:carbs :pasta} r1)
  (search {} recipes)
  )
