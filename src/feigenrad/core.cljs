(ns feigenrad.core
  (:require [goog.object :as gobj]
            [reagent.core :as r]))

(defn presently []
  (js/Promise. (fn [resolve]
                 (js/setTimeout resolve 2000))))

(defn make-signal []
  (atom false))

(defn check-signal-fn [signal]
  (fn [v]
    (when @signal
      (prn ::cancelled)
      (throw (js/Error. "Banana")))
    v))

(defn ping []
  (prn ::ping))

(defn abort []
  (prn ::abort))

(defn start []
  (prn ::start)
  (let [signal (make-signal)]
    (-> (presently)
        (.then (check-signal-fn signal))
        (.then ping))))

(defn root []
  [:main.container
   [:div.form-group
    [:label.control-label {:for "foo"} "Start"]
    [:button#foo.form-control
     {:on-click start}
     "Start"]]
   [:div.form-group
    [:label.control-label {:for "foo"} "Abort"]
    [:button#foo.form-control
     {:on-click abort}
     "Abort"]]])

(defn run []
  (r/render [root] (js/document.getElementById "app")))

(run)

(defn on-js-reload []
  )
