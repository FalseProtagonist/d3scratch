(ns chestnut.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require 
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]
   [sablono.core :as h :refer-macros [html]]
   [cljs.core.async :refer 
    [<! chan put! sliding-buffer sub pub timeout]]
   [chestnut.graph :refer [graph-component]]
   [chestnut.notes :refer [button-component create-button]]))

(defonce app-state (atom {:text "Application State"}))

(defn main [] 
  (om/root
   (fn [app owner]
     (reify
       om/IInitState
       (init-state [_]
         {:refresh (chan) 
          :clear (chan) 
          :test "root initstate" 
          :appstate (:text app)})
       om/IRenderState
       (render-state [this mystate]
         (html [:div [:div 
                      [:p 
                       "local init state: " 
                       (:test mystate)]
                      [:p 
                       "button from local init state: "
                       #_(om/build button-component mystate nil)
                       (om/build 
                        (create-button #(put! (:refresh mystate) "closure")) 
                        mystate 
                        nil)]
                      [:p
                       (om/build 
                        (create-button #(put! (:clear mystate) "clear"))
                        mystate
                        nil)]
                      [:p "passed in state: " (:text app)]
                      [:p "owner state: " (om/get-state owner :appstate)]
                      [:p (om/build graph-component mystate nil)]]]))
       om/IWillMount
       (will-mount [_]
         #_(let [channel (om/get-state owner :refresh)]
           (go (loop []
                 (let [message (<! channel)]
                   #_(js/alert message)
                   (recur))))))
       om/IDidMount
       ( did-mount [_]
         nil))
)
   app-state
   {:target (. js/document (getElementById "app"))}))


