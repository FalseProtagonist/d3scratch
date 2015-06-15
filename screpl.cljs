(def width 960)
(def height 500)
(def color (-> js/d3 .-scale .category20))
(def nodes #js [1 2])
(def )
(def )
(def )
(def )

(let [width 960 height 500 
      color (-> js/d3 .-scale .category20)
      nodes #js [1 2]
      links #js {:source 0 :target 1}
      force  (-> js/d3 .-layout .force (.charge -120) (.linkDistance 30) (.size clj->js [100 100]) (.nodes nodes) (.links links) )
      svg (-> js/d3 (.select "div") (.append "svg") (.attr "width" width) (.attr "height" height))
      link (-> svg (.selectAll ".link") (.data links) .enter (.append "line") (.attr "class" "link"))
      node (-> svg (.selectAll ".node") (.data nodes) .enter (.append "circle") (.attr "class" "node"))
      ]
  (-> force (. on "end" 
               (fn (-> node 
                       (.attr "r" (/ width 25)) 
                       (.attr "cx" (fn [d]  (.- d x))) 
                       (.attr "cy" (fn [d] (.- d y))))))))



(let [rcolor (js/d3 -> .scale)])
(def force  (-> js/d3 .-layout .force (.charge -120) (.linkDistance 30) (.size clj->js [100 100])))

