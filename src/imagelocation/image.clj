(ns imagelocation.image
  (:require [clojure.pprint :as pp]
            [clojure.java.io :as io]
            [clojure.string :as str])
  (:import [com.drew.imaging ImageMetadataReader]))

;; ----------------------------------------------------------------------------------------------------
;; Resources:
;; https://www.linkedin.com/learning/code-clinic-clojure/parsing-exif-data?u=2324666
;; https://github.com/joshuamiller/exif-processor/blob/master/src/exif_processor/core.clj
;; https://gis.stackexchange.com/questions/136925/how-to-parse-exif-gps-information-to-lat-lng-decimal-numbers
;; https://developers.google.com/maps/documentation/urls/guide
;; https://github.com/drewnoakes/metadata-extractor

(defn- extract-from-tag
  [tag]
  (into {} (map #(hash-map (.getTagName %) (.getDescription %)) tag)))

(defn image-data
  "Return map of all image data fields
"
  [filename]
  (try
      (->> (io/file filename)
       ImageMetadataReader/readMetadata
       .getDirectories
       (map #(.getTags %))
       (into {} (map extract-from-tag)))
      (catch com.drew.imaging.ImageProcessingException e
        (pp/pprint (.getMessage e)))))

(defn get-location-data
  "Return `GPS Latitude` and `GPS Longitude` values in a map
"
  [filename]
  (let [info (image-data filename)]
    {:lat (get info "GPS Latitude")
     :lng (get info "GPS Longitude")}))

(defn just-numbers
  "GPS values have symbols we need to remove. 

For example here is a Latitude value:

  40° 42' 40.24\"

This routine should take this example and return

  [40 42 40.24]
"
  [v]
  (mapv #(read-string %) (str/split (apply str (filter #(or (Character/isDigit %) (Character/isSpaceChar %) (= % \-) (= % \.)) v)) #" ")))

(defn abs [n] (max n (- n)))

(defn single-number 
  "Convert the three number location to a single value.

Note: You need to add the negative back after the calculation. If you process with it the result is wrong.
"
  [[degrees minutes seconds]]
  (let [neg (< degrees 0)
        val (+ (abs degrees) (/ minutes 60) (/ seconds 3600))]       ;;(format "%.6f" (+ degrees (/ minutes 60) (/ seconds 3600)))
    (if neg
      (- 0 val)
      val)))

(defn single-number-lat-lng
  "Conver our lat/lng map values from original strings to single numbers
"
  [{:keys [lat lng]}]
  (let [lat (single-number (just-numbers lat))
        lng (single-number (just-numbers lng))]
    {:lat lat
     :lng lng}))


(defn build-map-link 
  "Build a Googe Map link

https://www.google.com/maps/search/?api=1&query=lat,lng
"
  [{:keys [lat lng]}]
  (format "https://www.google.com/maps/search/?api=1&query=%s,%s" lat lng))


(defn process
  "Process an image and return it's lat and lng values as well as a
Google Map link
"
  [filename]
  (let [location (get-location-data filename) 
        data (if (and (:lat location) (:lng location)) (single-number-lat-lng location) location)
        link (if (and (:lat data) (:lng data)) (build-map-link data))]
    (assoc data :link link)))
