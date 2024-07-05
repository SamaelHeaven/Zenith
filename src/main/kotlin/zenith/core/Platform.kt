package zenith.core

enum class Platform {
    DESKTOP,
    WEB;

    companion object {
        val current = if (System.getProperty("zenith.webBuild") == "true") {
            WEB
        } else {
            DESKTOP
        }
    }
}