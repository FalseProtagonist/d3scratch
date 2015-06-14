(def force  (-> js/d3 .-layout .force (.charge -120) (.linkDistance 30) (.size clj->js [100 100])))
