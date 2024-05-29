package org.samaelheaven.zenith.utils

object Path {
    fun format(path: String): String {
        var result = path.replace("\\", "/")
        result = result.replace(Regex("/+"), "/")
        while (result.startsWith("/")) {
            result = result.substring(1)
        }
        while (result.endsWith("/")) {
            result = result.substring(0, result.length - 1)
        }
        return result
    }
}