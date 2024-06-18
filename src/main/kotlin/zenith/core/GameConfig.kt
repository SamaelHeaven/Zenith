package zenith.core

import javafx.beans.NamedArg
import java.awt.GraphicsEnvironment
import java.net.URL

data class GameConfig(
    @NamedArg("width") val width: Int = 800,
    @NamedArg("height") val height: Int = 600,
    @NamedArg("fpsTarget") val fpsTarget: Int = GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices[0].getDisplayMode().refreshRate,
    @NamedArg("title") val title: String = "Game",
    @NamedArg("fullscreen") val fullscreen: Boolean = false,
    @NamedArg("decorated") val decorated: Boolean = true,
    @NamedArg("resizable") val resizable: Boolean = true,
    @NamedArg("icon") val icon: URL? = null
)