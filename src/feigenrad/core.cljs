(ns feigenrad.core
  (:require [goog.object :as gobj]))

;; Demo of using bare React using ES6 classes (without createClass)
;;
;; Uses React 16
;;
;; h/t Thomas Heller
;; https://gist.github.com/thheller/7f530b34de1c44589f4e0671e1ef7533#file-es6-class-cljs-L18

(enable-console-print!)

(prn [:foo])

(defn my-component [props context updater]
  (cljs.core/this-as this
    (js/React.Component.call this props context updater)
    ;; anything else you want to set-up. use goog.object/set on this
    (gobj/extend this #js{"render"
                          (fn []
                            (js/React.createElement "div" #js{} #js["Welcome!!"]))})
    this))

(gobj/extend
    (.. my-component -prototype)
  js/React.Component.prototype)

;; cljs-specific properties on constructor
;; just so (prn my-component) works properly, not actually required
(set! (.-cljs$lang$type my-component) true)
(set! (.-cljs$lang$ctorStr my-component) "MyComponent")
(set! (.-cljs$lang$ctorPrWriter my-component)
      (fn [this writer opt]
        (cljs.core/-write writer "MyComponent")))
(set! (.. my-component -prototype -constructor) my-component)

(defn run []
  (js/ReactDOM.render (js/React.createElement my-component) (js/document.getElementById "app")))

(run)

(defn on-js-reload []
  )
