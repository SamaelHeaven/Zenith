package zenith.core

import javafx.animation.AnimationTimer
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCombination
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import zenith.asset.Texture
import zenith.input.Keyboard
import zenith.input.Mouse
import zenith.math.Vector2
import java.io.PrintStream

object Game {
    private lateinit var _scene: Scene
    private lateinit var _fxStage: Stage
    private lateinit var _fxCanvas: Canvas
    private lateinit var config: GameConfig
    private lateinit var gameLoop: GameLoop
    private lateinit var fxScene: javafx.scene.Scene
    private lateinit var fxRoot: StackPane
    private var _icon: Texture? = null
    private var _fpsTarget: Int = 0
    private var initialized: Boolean = false
    private var currentScene: Scene? = null
    private val err = System.err

    var scene: Scene
        get() {
            throwIfUninitialized()
            return currentScene ?: _scene
        }
        set(value) {
            throwIfUninitialized()
            _scene = value
        }

    val width: Int
        get() {
            throwIfUninitialized()
            return config.width
        }

    val height: Int
        get() {
            throwIfUninitialized()
            return config.height
        }

    val size: Vector2
        get() {
            throwIfUninitialized()
            return Vector2(width, height)
        }

    val icon: Texture?
        get() {
            throwIfUninitialized()
            return _icon
        }

    val title: String
        get() {
            throwIfUninitialized()
            return config.title
        }

    val decorated: Boolean
        get() {
            throwIfUninitialized()
            return config.decorated
        }

    val focused: Boolean
        get() {
            throwIfUninitialized()
            return _fxStage.isFocused
        }

    var fullscreen: Boolean
        get() {
            throwIfUninitialized()
            return _fxStage.isFullScreen
        }
        set(value) {
            throwIfUninitialized()
            _fxStage.isFullScreen = value
        }

    var fpsTarget: Int
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
            run()
        }
    }

    fun exit() {
        throwIfUninitialized()
        gameLoop.stop()
        _fxStage.isFullScreen = false
        _fxStage.close()
    }

    fun screenshot(): Texture {
        throwIfUninitialized()
        return Texture(_fxCanvas.snapshot(null, null))
    }

    internal fun throwIfUninitialized() {
        if (!initialized) {
            throw IllegalStateException("Game has not been initialized yet.")
        }
    }

    internal val fxStage: Stage get() = _fxStage

    internal val fxCanvas: Canvas get() = _fxCanvas

    private fun initialize() {
        try {
            initializeRoot()
            initializeCanvas()
            initializeScene()
            initializeStage()
        } catch (e: Exception) {
            e.printStackTrace(err)
        } finally {
            System.setErr(err)
        }
    }

    private fun initializeRoot() {
        fxRoot = StackPane()
        fxRoot.background = Background(BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))
    }

    private fun initializeCanvas() {
        _fxCanvas = Canvas(width.toDouble(), height.toDouble())
        fxRoot.children.add(_fxCanvas)
    }

    private fun initializeScene() {
        fxScene = javafx.scene.Scene(fxRoot, width.toDouble(), height.toDouble())
        val resizedListener = {
            val scaleX = fxScene.width / width.toDouble()
            val scaleY = fxScene.height / height.toDouble()
            val scale = scaleX.coerceAtMost(scaleY)
            _fxCanvas.scaleX = scale
            _fxCanvas.scaleY = scale
        }
        fxScene.widthProperty().addListener { _, _, _ -> resizedListener() }
        fxScene.heightProperty().addListener { _, _, _ -> resizedListener() }
    }

    private fun initializeStage() {
        _fxStage = Stage()
        _fxStage.initStyle(if (config.decorated) StageStyle.DECORATED else StageStyle.UNDECORATED)
        _fxStage.title = config.title
        _fxStage.fullScreenExitHint = ""
        _fxStage.fullScreenExitKeyCombination = KeyCombination.NO_MATCH
        _fxStage.isFullScreen = config.fullscreen
        _fxStage.isResizable = config.resizable
        config.icon?.let {
            _icon = Texture(it)
            _fxStage.icons.add(_icon!!.fxImage)
        }
        _fxStage.scene = fxScene
        _fxStage.setOnCloseRequest { exit() }
        _fxStage.centerOnScreen()
        _fxStage.show()
    }

    private fun run() {
        var cleanup = false
        gameLoop = GameLoop {
            Time.update()
            Keyboard.update()
            Mouse.update()
            if (cleanup) {
                System.gc()
                cleanup = false
            }
            if (currentScene != _scene) {
                currentScene = _scene
                currentScene?.start()
                cleanup = true
            }
            currentScene?.update()
        }
        gameLoop.start()
    }

    private class GameLoop(val callback: () -> Unit) : AnimationTimer() {
        override fun handle(arg: Long) {
            callback()
        }
    }
}