package org.samaelheaven.zenith.asset

import javafx.scene.image.Image
import java.net.URL

class Texture(url: URL) {
    internal val fxImage: Image = Image(url.toExternalForm())
    val width: Float = fxImage.width.toFloat()
    val height: Float = fxImage.height.toFloat()
}