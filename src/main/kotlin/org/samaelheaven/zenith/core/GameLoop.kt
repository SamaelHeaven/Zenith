package org.samaelheaven.zenith.core

import javafx.animation.AnimationTimer

internal class GameLoop(val callback: () -> Unit) : AnimationTimer() {
    override fun handle(arg: Long) {
        callback()
    }
}