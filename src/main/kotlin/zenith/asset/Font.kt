package zenith.asset

import javafx.beans.NamedArg
import javafx.scene.text.Font
import zenith.core.Game
import zenith.io.Resource
import java.net.URL
import java.util.*

class Font {
    internal val fxFont: Font
    val size get() = fxFont.size

    companion object {
        private val cachedFonts = mutableMapOf<Pair<String, Float>, Font>()
        const val DEFAULT_SIZE = 12f
        val default = Font(Font.font(DEFAULT_SIZE.toDouble()))
        var cacheByDefault = true

        @JvmStatic
        fun valueOf(value: String): zenith.asset.Font {
            val scanner = Scanner(value)
            val path = scanner.next()
            var size = DEFAULT_SIZE
            if (scanner.hasNextFloat()) {
                size = scanner.nextFloat()
            }
            return Font(path, size)
        }
    }

    constructor(
        @NamedArg("url") url: URL,
        @NamedArg("size") size: Float = DEFAULT_SIZE,
        @NamedArg("cache") cache: Boolean = cacheByDefault
    ) {
        val path = url.toExternalForm()
        val key = Pair(path, size)
        if (cachedFonts.containsKey(key)) {
            fxFont = cachedFonts[key]!!
            return
        }
        fxFont = Font.loadFont(path, size.toDouble())
        if (cache) {
            cachedFonts[key] = fxFont
        }
    }

    constructor(
        @NamedArg("resource") resource: String,
        @NamedArg("size") size: Float = DEFAULT_SIZE,
        @NamedArg("cache") cache: Boolean = cacheByDefault
    ) : this(
        Resource.url(resource), size, cache
    )

    internal constructor(fxFont: Font) {
        this.fxFont = fxFont
    }

    init {
        Game.throwIfUninitialized()
    }
}