package org.samaelheaven.zenith.core

import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.stage.Stage

class App : Application() {
    companion object {
        fun launch() {
            launch(App::class.java)
        }
    }

    override fun start(stage: Stage) {
        println("It works!!!!!")
        val root = Group()
        val scene = Scene(root, 800.0, 600.0)
        stage.scene = scene
        stage.show()
    }
}