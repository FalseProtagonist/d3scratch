(clj->js [{:source 0 :target 1} {:source 0 :target 2}])

nodes (clj->js 
               [{:x (/ width 3) :y (/  height 2)} 
                {:x (* 2 (/ width 3)) :y (/ height 2)}
                {:x width :y height}])



