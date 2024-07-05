package zenith.core

enum class Platform {
    DESKTOP,
    WEB;

    companion object {
        fun get(): Platform {
            if (System.getProperty("zenith.webBuild") == "true") {
                return WEB
            }
            return DESKTOP
        }
    }
}