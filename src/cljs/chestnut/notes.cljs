(ns chestnut.notes
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as h :refer-macros [html]]
            [cljs.core.async :refer [<! chan put! sliding-buffer sub pub timeout]]))

(defn workingd3 []
  (-> js/d3 (.select "div") (.selectAll "p") (.data (clj->js ["hello" "world"])) .enter (.append "p") (.text "hello")))

(defn button-component [data owner]
  (reify
    om/IRenderState
    (render-state [_ val]
      (html [:button data]
            ))))

(defn button-component2 [data owner]
  (reify
    om/IRenderState
    (render-state [_ val]
      (let [channel (:refresh data)] 
        (html [:button  
               {:on-click 
                (fn [e] (put! channel "hello from your channel")
                  )
                }
               (:test data)]
              )))))


(defn makeashape [] 
  (-> js/d3 
      (.select "div") 
      (.append "svg") 
      (.attr "width" 500) 
      (.attr "height" 500) 
      (.selectAll "circle")
      (.data (clj->js (range 10)))
      .enter
      (.append "circle")
      (.attr "cy" (fn [d-] (* 10 d- d-))) 
      (.attr "cx" 100) 
      (.attr "r" (fn [d-] (* 10 d-)))))
