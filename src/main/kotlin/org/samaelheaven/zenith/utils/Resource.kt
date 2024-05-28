package org.samaelheaven.zenith.utils

import java.io.InputStream
import java.net.URL

object Resource {
    fun url(path: String): URL {
        return Resource::class.java.getResource(formatPath(path))!!
    }

    fun stream(path: String): InputStream {
        return Resource::class.java.getResourceAsStream(formatPath(path))!!
    }

    private fun formatPath(path: String): String {
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
}