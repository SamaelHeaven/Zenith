package zenith.core

import java.net.URL

data class GameConfig(
    val width: Int = 800,
    val height: Int = 600,
    val fpsTarget: Int = 60,
    val title: String = "Game",
    val fullscreen: Boolean = false,
    val decorated: Boolean = true,
    val resizable: Boolean = true,
    val icon: URL? = null
)