(ns chestnut.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require 
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]
   [sablono.core :as h :refer-macros [html]]
   [cljs.core.async :refer 
    [<! chan put! sliding-buffer sub pub timeout]]
   [chestnut.graph :refer [graph-component]]
   [chestnut.notes :refer [button-component button-component2]]))

(defonce app-state (atom {:text "Hello world!"}))

(defn main [] 
  (om/root
   (fn [app owner]
     (reify
       om/IInitState
       (init-state [_]
         {:refresh (chan) :test "initstatebutton" :buttontext (:text app)})
       om/IRenderState
       (render-state [this mystate]
         (html [:div [:div 
                      [:button (:test mystate)]
                      [:p "second"]
                      [:p (om/build button-component (:test mystate) nil)]
                      [:p "third"]
                      [:p (om/build button-component2 mystate nil)]
                      [:button (:text app)]
                      [:button (om/get-state owner :buttontext)]
                      [:button "rerun"] 
                      [:p (om/build graph-component "passed" nil)]
                      [:p "world"]]]))
       om/IWillMount
       (will-mount [_]
         (let [delete (om/get-state owner :refresh)]
           (go (loop []
                 (let [contact (<! delete)]
                   (js/alert "wow")
                   (recur))))))
       om/IDidMount
       ( did-mount [_]
         nil))
)
   app-state
   {:target (. js/document (getElementById "app"))}))


