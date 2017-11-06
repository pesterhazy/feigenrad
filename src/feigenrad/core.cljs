(ns feigenrad.core
  (:require [goog.object :as gobj]
            [reagent.core :as r]))

(defn presently []
  (js/Promise. (fn [resolve]
                 (js/setTimeout resolve 2000))))

(def !signal (atom nil))

(defn make-signal []
  (atom false))

(defn dispatch-signal [signal]
  (reset! signal true))

(defn check-signal-fn [signal]
  (fn [v]
    (when @signal
      (prn ::canceled)
      (throw #js{:name "AbortSignal" :description "About signal"}))
    v))

(defn ping []
  (prn ::ping))

(defn abort []
  (prn ::abort)
  (some-> @!signal dispatch-signal))

(defn start []
  (prn ::start)
  (let [signal (make-signal)]
    (reset! !signal signal)
    (-> (presently)
        (.then (check-signal-fn signal))
        (.then ping)
        (.catch (fn [e]
                  (when-not (= "AbortSignal" (.-name e))
                    (throw e)))))))

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
