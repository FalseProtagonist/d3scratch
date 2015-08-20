(ns chestnut.graph
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as h :refer-macros [html]]
            [cljs.core.async :refer [<! chan put! sliding-buffer sub pub timeout]]))

(def clear-command (fn [] (-> 
                                  js/d3 
                                  (.select "svg") 
                                        ;(.selectAll "*") 
                                  .remove)))
(defn getrandomnodes [n width height]
  (clj->js (take n (repeatedly #(clj->js {
                                          :x (rand width) 
                                          :y (rand height) 
                                          :graph (rand-int 5)})))))

(defn getrandomlinks [n]
  (clj->js (for [source (range n) 
                 target (range n)
                 :when (= 0 (rand-int 20))]
             (clj->js {:source source :target target}))))


(defn force-layout []
  (let [width 1000 height 1000
        nodes (getrandomnodes 30 1000 1000)
        links (getrandomlinks 30)
        animationstep 400
        counter (atom 10)
        colourmap {0 "green" 1 "red"}
        svg (-> js/d3 
                (.select "div#map")
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
        updatenode (fn [] (-> node 
                              (.attr "cx" #(.-x %))
                              (.attr "cy" #(.-y %))
                              (.attr 
                               "fill" 
                               #(get 
                                 colourmap 
                                 (if (= 0 (.-graph %)) 0 1)))))
        updatelink  (fn [] (-> link
                               (.attr "x1" #(-> % .-source .-x))
                               (.attr "y1" #(-> % .-source .-y))
                               (.attr "x2" #(-> % .-target .-x))
                               (.attr "y2" #(-> % .-target .-y))))
        updateall #(do (updatenode) (updatelink))
        chilledupdate #(do 
                        (updateall) 
                        (.stop force) 
                        (js/setTimeout (.start force) animationstep ))
        force (-> 
               js/d3 
               .-layout 
               .force 
               (.size #js [width height])
               (.nodes nodes)
               (.links links)
               (.linkDistance (/ width 5))
               (.charge (fn [n] (get {0 1000 1 -1000} (.-graph n))))
               (.on "end"
                    nil
                    ;#(do (clear-command))
                    )
               (.on "tick" updateall )
               .start)]))

(defn graph-component [data owner]
  (reify 
    om/IInitState
    (init-state [_]
      {:graphstate "graph initstate"})      
    om/IRenderState
    (render-state [_ _]
      (do (html
           [:div#map
            [:p "first: " (om/get-state owner :graphstate)]
            [:p "second: " (:test data)]]))
      )
    om/IWillMount
    (will-mount [_]
      (let [refresh (:refresh data) clear (:clear data)]
        (go-loop []
          (let [message (<! refresh)] 
            (do (clear-command)
                (force-layout)
                (recur)
                ; (js/alert message)
                )))))
    om/IDidMount
    (did-mount [this] (force-layout))))


