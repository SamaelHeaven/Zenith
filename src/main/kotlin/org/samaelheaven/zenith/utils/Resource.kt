package org.samaelheaven.zenith.utils

import org.samaelheaven.zenith.asset.Texture
import java.io.InputStream
import java.net.URL

private val textures = HashMap<String, Texture>()

fun url(path: String): URL {
    return ClassLoader.getSystemResource(path)!!
}

fun stream(path: String): InputStream {
    return ClassLoader.getSystemResourceAsStream(path)!!
}

fun texture(path: String): Texture {
    return textures.getOrPut(path) { Texture(url(path)) }
}

fun path(path: String): String {
    var result = path.replace("\\", "/")
    result = result.replace(Regex("/+"), "/")
    while (result.startsWith("/")) {
        result = result.substring(1)
    }
    while (result.endsWith("/")) {
        result = result.substring(0, result.length - 1)
    }
    return "/$result"
}