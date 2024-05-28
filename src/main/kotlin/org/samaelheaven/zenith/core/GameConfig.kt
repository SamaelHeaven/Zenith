package org.samaelheaven.zenith.core

import org.samaelheaven.zenith.asset.Texture

data class GameConfig(
    val width: UInt = 800u,
    val height: UInt = 600u,
    val fpsTarget: UInt = 60u,
    val title: String = "Game",
    val fullscreen: Boolean = false,
    val decorated: Boolean = true,
    val resizable: Boolean = true,
    val icon: Texture? = null
)