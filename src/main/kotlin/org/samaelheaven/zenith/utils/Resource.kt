package org.samaelheaven.zenith.utils

import java.io.InputStream
import java.net.URL

fun url(path: String): URL {
    return ClassLoader.getSystemResource(path)!!
}

fun stream(path: String): InputStream {
    return ClassLoader.getSystemResourceAsStream(path)!!
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