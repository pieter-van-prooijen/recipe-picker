(ns recipe-picker.db
  (:require [re-frame.core :as re-frame :refer [reg-sub subscribe]]
            [clojure.string :as string]))

(def wild-card-value "*")

(def default-db
  {:all-recipes [{:title "recipe 1"
                  :location "book 1"
                  :tags {:carbs "Pasta"
                         :vegs "Erwten"
                         :misc "Anchovis"}}
                 {:title "recipe 2"
                  :location "book 2"
                  :tags {:carbs #{"Rijst"}
                         :vegs #{"Paprika" "Tomaat"}
                         :misc #{"Ananas"}}}
                 ]
   :shuffle 42
   :query {}})

(reg-sub ::all-recipes
         (fn [db _]
           (:all-recipes db)))

;; answer all values for the given tag
(reg-sub ::all-tag-values
         (fn [_ _]
           (subscribe [::all-recipes]))
         (fn [all-recipes [_ tag]]
           (let [all-tag-values (reduce (fn [r recipe]
                                          (if-let [tag-values (get-in recipe [:tags tag])]
                                            (into r tag-values)
                                            r))
                                        #{}
                                        all-recipes)]
             (->> all-tag-values
                  (remove string/blank?)
                  (sort)
                  (into [wild-card-value])))))

(reg-sub ::query
         (fn [db _]
           (:query db)))

(reg-sub ::shuffle
         (fn [db _]
           (:shuffle db)))

;; return the selected tag value or "*" if the tag was not selected
(reg-sub ::selected-tag-value
         (fn [_ _]
           (subscribe [::query]))
         (fn [query [_ tag]]
           (get query tag wild-card-value)))


;; query is a map of tag => #{tag-values}
(defn match-recipe [query recipe]
  (let [tags (:tags recipe)]
    (every? (fn [k]
              (contains? (k tags) (k query)))
            (keys query))))

(defn search [query recipes]
  (filter (partial match-recipe query) recipes))

(reg-sub ::filtered-recipes
         (fn [_ _]
           [(subscribe [::all-recipes])
            (subscribe [::query])
            (subscribe [::shuffle])])
         (fn [[all-recipes query shuffle-count] _]
           (let [result (search query all-recipes)]
             (if (pos? shuffle-count) ; increasing integer to trigger the subscription
               (shuffle result)
               result))))

(comment
  (def r1 {:title "recipe-1"
           :location "loc-1"
           :tags {:carbs #{:pasta :farfalle}
                  :veg #{:spinazie}}})
  (def r2 {:title "recipe-2"
           :location "loc-2"
           :tags {:carbs #{:rijst}
                  :veg #{:bloemkool}}})

  (def recipes [r1 r2])
  (keys {:carbs :pasta})
  (select-keys (:tags r1) [:carbs])
  (match-recipe {:carbs :pasta} r1)
  (search {} recipes)
  )
