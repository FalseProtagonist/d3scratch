(ns chestnut.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as h :refer-macros [html]]
            [cljs.core.async :refer 
             [<! chan put! sliding-buffer sub pub timeout]]
            [chestnut.graph :refer [graph-component]]
            [chestnut.notes :refer [button-component]]))



(defn main [] 
  (om/root
    (fn [app owner]
      (reify
        om/IInitState
        (init-state [_]
          {:delete (chan)})
        om/IRender
        (render [_]
          (html [:div [:div 
                       [:button "rerun"] 
                       [:p (om/build graph-component nil nil)]
                       [:p (om/build button-component nil nil)]
                       [:p "world"]]]))
        om/IDidMount
        ( did-mount [_]
          nil)))
    app-state
    {:target (. js/document (getElementById "app"))}))


