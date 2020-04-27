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
          (count))
     1))

;; fields can contain multiple values separated by a pipe
(defn parse-field [fields index]
  (let [field (get fields index)]
    (into #{} (string/split field #"\|"))))

(defn to-recipe [fields]
  ;; comment lines could be present
  (when (recipe? fields)
    {:title (get fields 0)
     :location (get fields 1)
     :tags (into {} (remove (fn [[_ field]]
                              (string/blank? field))
                            [[:carbs (parse-field fields 2)]
                             [:vegs (parse-field fields 3)]
                             [:misc (parse-field fields 4)]]))}))

(defn to-recipes [csv]
  (->> (parse-csv csv)
       (rest) ; skip the header
       (map to-recipe)
       (remove nil?)))

(comment
  (string/split  "a,b\nc,d" #"\n|\r\n")
  (string/split  "a,b" #",")
  (parse-csv "a,b\nc,d"))
