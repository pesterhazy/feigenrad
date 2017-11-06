(ns feigenrad.core
  (:require [goog.object :as gobj]
            [reagent.core :as r]))

(defn presently []
  (js/Promise. (fn [resolve]
                 (js/setTimeout resolve 1000))))

(defn ping []
  (prn ::ping))

(defn start []
  (prn ::start)
  (-> (presently)
      (.then ping)))

(defn root []
  [:main.container
   [:div.form-group
    [:label.control-label {:for "foo"} "Start"]
    [:button#foo.form-control
     {:on-click start}
     "Start"]]])

(defn run []
  (r/render [root] (js/document.getElementById "app")))

(run)

(defn on-js-reload []
  )
