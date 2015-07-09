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


(defn getrandomnodes [n width height]
  (clj->js (take n (repeatedly #(clj->js {:x (rand width) :y (rand height)})))))

(defn getrandomlinks [n]
  (clj->js (for [source (range n) 
                 target (range n)
                 :when (= 1 (rand-int 5))]
             (clj->js {:source source :target target}))))


(defn forcelayout []
  (let [width 1000 height 1000
        nodes (getrandomnodes 10 1000 1000)
        links (getrandomlinks 10)
        svg (-> js/d3 
                (.select "div")
                (.append "svg")
                (.attr "width" width)
                (.attr "height" height))
        link (-> 
              svg 
              (.selectAll "line") 
              (.data links)
              .enter
              (.append "line"))
        node (->
              svg
              (.selectAll "circle")
              (.data nodes)
              .enter
              (.append "circle")
              (.attr "r" 100)
              (.attr "cx" 100))
        force (-> 
               js/d3 
               .-layout 
               .force 
               (.size #js [width height])
               (.nodes nodes)
               (.links links)
               (.linkDistance (/ width 2))
               (.on "end"  (fn [] (do 
                                    (print "ended!")
                                    (-> link
                                        (.attr "x1" #(-> % .-source .-x))
                                        (.attr "y1" #(-> % .-source .-y))
                                        (.attr "x2" #(-> % .-target .-x))
                                        (.attr "y2" #(-> % .-target .-y))
                                        (.attr "stroke-width" 5)
                                        (.attr "stroke" "red"))
                                    (-> node
                                        (.attr "r" 10)
                                        (.attr "cx" #(.-x %) )
                                        (.attr "cy" #(.-y %))))))
               .start)]
    ))


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
          (forcelayout))))
    app-state
    {:target (. js/document (getElementById "app"))}))



