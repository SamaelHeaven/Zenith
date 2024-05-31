package zenith.core

import javafx.application.Platform
import javafx.event.EventHandler
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
import zenith.math.Vector2
import java.io.PrintStream

object Game {
    private lateinit var config: GameConfig
    private lateinit var _scene: Scene
    private lateinit var gameLoop: GameLoop
    private lateinit var fxStage: Stage
    private lateinit var fxScene: javafx.scene.Scene
    private lateinit var fxRoot: StackPane
    private lateinit var fxCanvas: Canvas
    private var currentScene: Scene? = null
    private var _icon: Texture? = null
    private var _fpsTarget: Int = 0
    private var initialized: Boolean = false
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
            return fxStage.isFocused
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
        fxStage.isFullScreen = false
        fxStage.close()
    }

    fun screenshot(): Texture {
        throwIfUninitialized()
        return Texture(fxCanvas.snapshot(null, null))
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
        initializeKeyboard()
        initializeStage()
        System.setErr(err)
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

    private fun initializeKeyboard() {
        fxScene.onKeyTyped = EventHandler { Keyboard.onKeyTyped(it) }
        fxScene.onKeyPressed = EventHandler { Keyboard.onKeyPressed(it) }
        fxScene.onKeyReleased = EventHandler { Keyboard.onKeyReleased(it) }
    }

    private fun initializeStage() {
        fxStage = Stage()
        fxStage.initStyle(if (config.decorated) StageStyle.DECORATED else StageStyle.UNDECORATED)
        fxStage.title = config.title
        fxStage.fullScreenExitHint = ""
        fxStage.fullScreenExitKeyCombination = KeyCombination.NO_MATCH
        fxStage.setOnCloseRequest { exit() }
        fxStage.isFullScreen = config.fullscreen
        fxStage.isResizable = config.resizable
        config.icon?.let {
            _icon = Texture(it)
            fxStage.icons.add(_icon!!.fxImage)
        }
        fxStage.scene = fxScene
        fxStage.centerOnScreen()
        fxStage.show()
    }

    private fun run() {
        var cleanup = false
        gameLoop = GameLoop {
            Time.update()
            Keyboard.update()
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
}