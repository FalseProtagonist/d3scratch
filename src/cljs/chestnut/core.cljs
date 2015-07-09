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
                 :when (= 1 (rand-int 2))]
             (clj->js {:source source :target target}))))


(defn forcelayout []
  (let [width 1000 height 1000
        nodes (getrandomnodes 10 1000 1000)
        links (getrandomlinks 10)
        animationstep 400
        counter (atom 10)
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
              (.append "line")
              (.attr "x1" #(.-x (get nodes (.-source %) )))
              (.attr "x2" #(.-x (get nodes (.-target %) )))
              (.attr "y1" #(.-y (get nodes (.-source %) )))
              (.attr "y2" #(.-y (get nodes (.-target %)  )))
              (.attr "stroke-width" 1)
              (.attr "stroke" "red"))
        node (->
              svg
              (.selectAll "circle")
              (.data nodes)
              .enter
              (.append "circle")
              (.attr "r" (/ width 100))
              (.attr "cx" #(.-x %))
              (.attr "cy" #(.-y %)))
        force (-> 
               js/d3 
               .-layout 
               .force 
               (.size #js [width height])
               (.nodes nodes)
               (.links links)
               (.linkDistance (/ width 5))
               (.on "end"  (fn [] (do 
                                    (print "ended!")
                                    (-> link
                                        (.attr "x1" #(-> % .-source .-x))
                                        (.attr "y1" #(-> % .-source .-y))
                                        (.attr "x2" #(-> % .-target .-x))
                                        (.attr "y2" #(-> % .-target .-y)))
                                    (-> node
                                        (.attr "r" 10)
                                        (.attr "cx" #(.-x %) )
                                        (.attr "cy" #(.-y %))))))
               (.on "tick" (fn [] (do
                                    (-> node (.attr "cx" #(.-x %)))
                                    (-> node (.attr "cy" #(.-y %))))))
               .start)]))


(defn main []
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



