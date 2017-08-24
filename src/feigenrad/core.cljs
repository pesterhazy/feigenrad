(ns feigenrad.core
  (:require [goog.object :as gobj]))

;; Demo of using bare React using ES6 classes (without createClass)
;;
;; Uses React 16
;;
;; h/t Thomas Heller
;; https://gist.github.com/thheller/7f530b34de1c44589f4e0671e1ef7533#file-es6-class-cljs-L18

(enable-console-print!)

(defn make-component
  ([display-name m] (make-component display-name nil m))
  ([display-name construct m]
   (let [cmp (fn [props context updater]
               (cljs.core/this-as this
                 (js/React.Component.call this props context updater)
                 (when construct
                   (construct this))
                 this))]
     (gobj/extend (.-prototype cmp) js/React.Component.prototype m)

     (when display-name
       (set! (.-displayName cmp) display-name)
       (set! (.-cljs$lang$ctorStr cmp) display-name)
       (set! (.-cljs$lang$ctorPrWriter cmp)
             (fn [this writer opt]
               (cljs.core/-write writer display-name))))
     (set! (.-cljs$lang$type cmp) true)
     (set! (.. cmp -prototype -constructor) cmp))))

(def create-element js/React.createElement)

(def my-component
  (make-component "MyComponent"
                  (fn [this] (set! (.-state this) #js{:counter 0}))
                  #js{:render
                      (fn []
                        (this-as this
                          (create-element "div"
                                          nil
                                          #js[(create-element "div"
                                                              #js{:key 1}
                                                              #js["Counter is " (-> this .-state .-counter)])
                                              (create-element "button"
                                                              #js{:key 2
                                                                  :onClick #(.setState this
                                                                                       (fn [old]
                                                                                         #js{:counter (-> old .-counter inc)}))}
                                                              #js["Click me"])])))}))

(defn run []
  (js/ReactDOM.render (create-element my-component) (js/document.getElementById "app")))

(run)

(defn on-js-reload []
  )
