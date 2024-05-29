package zenith.utils

import java.io.InputStream
import java.net.URL

object Resource {
    fun url(path: String): URL {
        return Resource::class.java.getResource("/" + Path.format(path))!!
    }

    fun stream(path: String): InputStream {
        return Resource::class.java.getResourceAsStream("/" + Path.format(path))!!
    }
}