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
import org.samaelheaven.zenith.asset.Texture
import java.io.PrintStream

object Game {
    private lateinit var config: GameConfig
    private lateinit var _scene: Scene
    private lateinit var gameLoop: GameLoop
    private lateinit var fxStage: Stage
    private lateinit var fxScene: javafx.scene.Scene
    private lateinit var fxRoot: StackPane
    private lateinit var fxCanvas: Canvas
    private var _fpsTarget: UInt = 0u
    private var cleanup: Boolean = false
    private var initialized: Boolean = false
    private val err = System.err

    var scene: Scene
        get() {
            throwIfUninitialized()
            return _scene
        }
        set(value) {
            throwIfUninitialized()
            _scene = value
            _scene.start()
            cleanup = true
        }

    val width: UInt
        get() {
            throwIfUninitialized()
            return config.width
        }

    val height: UInt
        get() {
            throwIfUninitialized()
            return config.height
        }

    var fullscreen: Boolean
        get() {
            throwIfUninitialized()
            return fxStage.isFullScreen
        }
        set(value) {
            throwIfUninitialized()
            fxStage.isFullScreen = value
        }

    var fpsTarget: UInt
        get() {
            throwIfUninitialized()
            return _fpsTarget
        }
        set(value) {
            throwIfUninitialized()
            _fpsTarget = value
        }

    fun launch(scene: Scene, config: GameConfig = GameConfig()) {
        if (initialized) {
            throw IllegalStateException("Game has already been initialized.")
        }
        initialized = true
        this.config = config
        _fpsTarget = config.fpsTarget
        _scene = scene
        System.setErr(PrintStream(PrintStream.nullOutputStream()))
        Platform.startup {
            initialize()
            System.setErr(err)
            run()
        }
    }

    fun exit() {
        throwIfUninitialized()
        Platform.runLater {
            gameLoop.stop()
            Platform.exit()
        }
    }

    internal fun throwIfUninitialized() {
        if (!initialized) {
            throw IllegalStateException("Game has not been initialized yet.")
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
            fxStage.icons.add(Texture(it).fxImage)
        }
        fxStage.scene = fxScene
        fxStage.centerOnScreen()
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
            _scene.update()
        }
        gameLoop.start()
    }
}