package org.samaelheaven.zenith.core

import javafx.application.Platform
import javafx.geometry.Insets
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.io.PrintStream

class Game private constructor() {
    private lateinit var config: GameConfig
    private lateinit var scene: Scene
    private lateinit var gameLoop: GameLoop
    private lateinit var fxStage: Stage
    private lateinit var fxScene: javafx.scene.Scene
    private lateinit var fxRoot: StackPane
    private lateinit var fxCanvas: Canvas
    private var fpsTarget: UInt = 0u
    private var cleanup: Boolean = false

    companion object {
        private var instance: Game? = null
        private val err = System.err
        private val game: Game
            get() = (if (instance == null) Game() else instance).also { instance = it }!!

        var scene: Scene
            get() {
                throwIfUninitialized()
                return game.scene
            }
            set(value) {
                throwIfUninitialized()
                val game = this.game
                game.scene = value
                game.scene.start()
                game.cleanup = true
            }

        val width: UInt
            get() {
                throwIfUninitialized()
                return game.config.width
            }

        val height: UInt
            get() {
                throwIfUninitialized()
                return game.config.height
            }

        var fullscreen: Boolean
            get() {
                throwIfUninitialized()
                return game.fxStage.isFullScreen
            }
            set(value) {
                throwIfUninitialized()
                game.fxStage.isFullScreen = value
            }

        var fpsTarget: UInt
            get() {
                throwIfUninitialized()
                return game.fpsTarget
            }
            set(value) {
                throwIfUninitialized()
                game.fpsTarget = value
            }

        fun launch(scene: Scene, config: GameConfig = GameConfig()) {
            if (instance != null) {
                throw IllegalStateException("Game has already been initialized.")
            }
            val game = this.game
            game.config = config
            game.fpsTarget = config.fpsTarget
            game.scene = scene
            System.setErr(PrintStream(PrintStream.nullOutputStream()))
            Platform.startup {
                game.initialize()
                System.setErr(err)
                game.run()
            }
        }

        fun exit() {
            throwIfUninitialized()
            game.gameLoop.stop()
            Platform.runLater { Platform.exit() }
        }

        internal fun throwIfUninitialized() {
            if (instance == null) {
                throw IllegalStateException("Game has not been initialized yet.")
            }
        }
    }

    private fun initialize() {
        initializeRoot()
        initializeCanvas()
        initializeScene()
        initializeRenderer()
        initializeStage()
    }

    private fun initializeStage() {
        fxStage = Stage()
        fxStage.initStyle(if (config.decorated) StageStyle.DECORATED else StageStyle.UNDECORATED)
        fxStage.title = config.title
        fxStage.isFullScreen = config.fullscreen
        fxStage.isResizable = config.resizable
        config.icon?.let {
            fxStage.icons.add(it.fxImage)
        }
        fxStage.scene = fxScene
        fxStage.show()
    }

    private fun initializeRoot() {
        fxRoot = StackPane()
        fxRoot.background = Background(BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))
    }

    private fun initializeCanvas() {
        fxCanvas = Canvas(width.toDouble(), height.toDouble())
        fxRoot.children.add(fxCanvas)
    }

    private fun initializeScene() {
        fxScene = javafx.scene.Scene(fxRoot, width.toDouble(), height.toDouble())
        val resizedListener = {
            val scaleX = fxScene.width / width.toDouble()
            val scaleY = fxScene.height / height.toDouble()
            val scale = scaleX.coerceAtMost(scaleY)
            fxCanvas.scaleX = scale
            fxCanvas.scaleY = scale
        }
        fxScene.widthProperty().addListener { _, _, _ -> resizedListener() }
        fxScene.heightProperty().addListener { _, _, _ -> resizedListener() }
    }

    private fun initializeRenderer() {
        Renderer.initialize(fxCanvas)
    }

    private fun run() {
        gameLoop = GameLoop {
            Time.update()
            if (cleanup) {
                System.gc()
                cleanup = false
            }
            scene.update()
        }
        gameLoop.start()
    }
}