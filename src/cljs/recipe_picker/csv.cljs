(ns recipe-picker.csv
  (:require [clojure.string :as string]))

(defn parse-csv
  "Read a string of un-quoted comma-separated values and return a vector of vectors"
  [s]
  (->> (string/split s #"\n|\r\n")
       (remove string/blank?)
       (mapv (fn [line]
               (string/split line #",")))))

(defn recipe? [fields]
  (> (->> fields
          (remove string/blank?)
          (count)) 1))

(defn to-recipe [fields]
  ;; comment lines could be present
  (when (recipe? fields)
    {:title (get fields 0)
     :location (get fields 1)
     :tags (into {} (remove (fn [[_ field]]
                              (string/blank? field))
                            [[:carbs (get fields 2)]
                             [:vegs (get fields 3)]
                             [:misc (get fields 4)]]))}))

(defn to-recipes [csv]
  (->> (parse-csv csv)
       (rest) ; skip the header
       (map to-recipe)
       (remove nil?)))

(comment
  (string/split  "a,b\nc,d" #"\n|\r\n")
  (string/split  "a,b" #",")
  (parse-csv "a,b\nc,d"))
