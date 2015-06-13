(ns chestnut.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as h :refer-macros [html]]
            [cljs.core.async :refer [<! chan put! sliding-buffer sub pub timeout]]))
(defonce app-state (atom {:text "Hello D3Scratch!!"}))


(def x 4)
(def TIME_FORMAT
  (-> js/d3 .-time (.format "%Y/%m")))

(defn workingd3 []
  (-> js/d3 (.select "div") (.selectAll "p") (.data (clj->js ["hello" "world"])) .enter (.append "p") (.text "hello")))

(defn main []
  ()
  (om/root
    (fn [app owner]
      (reify
        om/IRender
        (render [_]
          (let [element
                (dom/div #js {:id "marc"} 
                         (dom/h1 nil (:text app)))]
            element))
        om/IDidMount
        ( did-mount [_]
          (workingd3))))
    app-state
    {:target (. js/document (getElementById "app"))}))



