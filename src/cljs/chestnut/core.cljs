(ns chestnut.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as h :refer-macros [html]]
            [cljs.core.async :refer [<! chan put! sliding-buffer sub pub timeout]]))
(defonce app-state (atom {:text "Hello D3Scratch7!"}))


(def x 4)
(def TIME_FORMAT
  (-> js/d3 .-time (.format "%Y/%m")))

(defn main []
  ()
  (om/root
    (fn [app owner]
      (reify
        om/IRender
        (render [_]
          (dom/h1 nil (:text app)))))
    app-state
    {:target (. js/document (getElementById "app"))}))



