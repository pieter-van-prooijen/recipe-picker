(ns recipe-picker.db
  (:require [re-frame.core :as re-frame :refer [reg-sub subscribe]]))

(def default-db
  {:all-recipes [{:title "recipe 1"
                  :location "book 1"
                  :tags {:carbs "Pasta"
                         :vegs "Erwten"
                         :misc "anchovis"}}
                 {:title "recipe 2"
                  :location "book 2"
                  :tags {:carbs "Rijst"
                         :vegs "Paprika"
                         :misc "Ananas"}}
                 ]})

(reg-sub ::all-recipes
         (fn [db _]
           (:all-recipes db)))

(reg-sub ::all-tag-values
         (fn [_ _]
           (subscribe [::all-recipes]))
         (fn [all-recipes [_ tag]]
           (println "tags: " tag " all-recipes sub: "  all-recipes)
           (reduce (fn [r recipe]
                     (conj r (get-in recipe [:tags tag])))
                   #{"*"}
                   all-recipes)))

(defn match-recipe [query recipe]
  (let [tags (:tags recipe)
        selected-tags (select-keys tags (keys query))]
    (= selected-tags query)))

(defn search [query recipes]
  (filter (partial match-recipe query) recipes))


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
  (search {:veg :bloemkool} recipes)
  )
