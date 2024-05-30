package zenith.paint

import javafx.beans.NamedArg
import java.util.*

class Color private constructor(override val fxPaint: javafx.scene.paint.Paint) : Paint() {
    val r: Int get() = (255 * (fxPaint as javafx.scene.paint.Color).red).toInt()
    val g: Int get() = (255 * (fxPaint as javafx.scene.paint.Color).green).toInt()
    val b: Int get() = (255 * (fxPaint as javafx.scene.paint.Color).blue).toInt()
    val a: Int get() = (255 * (fxPaint as javafx.scene.paint.Color).opacity).toInt()

    constructor(@NamedArg("web") web: String) : this(javafx.scene.paint.Color.web(web))

    constructor(
        @NamedArg("r") r: Int, @NamedArg("g") g: Int, @NamedArg("b") b: Int, @NamedArg("a") a: Int = 255
    ) : this(javafx.scene.paint.Color.rgb(r, g, b, a.toDouble() / 255))

    override fun toString(): String {
        return "{ \"r\": $r, \"g\": $g, \"b\": $b, \"a\": $a }"
    }

    override fun equals(other: Any?): Boolean {
        if (other is Color) {
            return r == other.r && g == other.g && b == other.b && a == other.a
        }
        return false
    }

    override fun hashCode(): Int {
        var result = r
        result = 31 * result + g
        result = 31 * result + b
        result = 31 * result + a
        return result
    }

    companion object {
        val TRANSPARENT = NamedColor(Color(javafx.scene.paint.Color.TRANSPARENT), "TRANSPARENT").color
        val ALICEBLUE = NamedColor(Color(javafx.scene.paint.Color.ALICEBLUE), "ALICEBLUE").color
        val ANTIQUEWHITE = NamedColor(Color(javafx.scene.paint.Color.ANTIQUEWHITE), "ANTIQUEWHITE").color
        val AQUA = NamedColor(Color(javafx.scene.paint.Color.AQUA), "AQUA").color
        val AQUAMARINE = NamedColor(Color(javafx.scene.paint.Color.AQUAMARINE), "AQUAMARINE").color
        val AZURE = NamedColor(Color(javafx.scene.paint.Color.AZURE), "AZURE").color
        val BEIGE = NamedColor(Color(javafx.scene.paint.Color.BEIGE), "BEIGE").color
        val BISQUE = NamedColor(Color(javafx.scene.paint.Color.BISQUE), "BISQUE").color
        val BLACK = NamedColor(Color(javafx.scene.paint.Color.BLACK), "BLACK").color
        val BLANCHEDALMOND = NamedColor(Color(javafx.scene.paint.Color.BLANCHEDALMOND), "BLANCHEDALMOND").color
        val BLUE = NamedColor(Color(javafx.scene.paint.Color.BLUE), "BLUE").color
        val BLUEVIOLET = NamedColor(Color(javafx.scene.paint.Color.BLUEVIOLET), "BLUEVIOLET").color
        val BROWN = NamedColor(Color(javafx.scene.paint.Color.BROWN), "BROWN").color
        val BURLYWOOD = NamedColor(Color(javafx.scene.paint.Color.BURLYWOOD), "BURLYWOOD").color
        val CADETBLUE = NamedColor(Color(javafx.scene.paint.Color.CADETBLUE), "CADETBLUE").color
        val CHARTREUSE = NamedColor(Color(javafx.scene.paint.Color.CHARTREUSE), "CHARTREUSE").color
        val CHOCOLATE = NamedColor(Color(javafx.scene.paint.Color.CHOCOLATE), "CHOCOLATE").color
        val CORAL = NamedColor(Color(javafx.scene.paint.Color.CORAL), "CORAL").color
        val CORNFLOWERBLUE = NamedColor(Color(javafx.scene.paint.Color.CORNFLOWERBLUE), "CORNFLOWERBLUE").color
        val CORNSILK = NamedColor(Color(javafx.scene.paint.Color.CORNSILK), "CORNSILK").color
        val CRIMSON = NamedColor(Color(javafx.scene.paint.Color.CRIMSON), "CRIMSON").color
        val CYAN = NamedColor(Color(javafx.scene.paint.Color.CYAN), "CYAN").color
        val DARKBLUE = NamedColor(Color(javafx.scene.paint.Color.DARKBLUE), "DARKBLUE").color
        val DARKCYAN = NamedColor(Color(javafx.scene.paint.Color.DARKCYAN), "DARKCYAN").color
        val DARKGOLDENROD = NamedColor(Color(javafx.scene.paint.Color.DARKGOLDENROD), "DARKGOLDENROD").color
        val DARKGRAY = NamedColor(Color(javafx.scene.paint.Color.DARKGRAY), "DARKGRAY").color
        val DARKGREEN = NamedColor(Color(javafx.scene.paint.Color.DARKGREEN), "DARKGREEN").color
        val DARKKHAKI = NamedColor(Color(javafx.scene.paint.Color.DARKKHAKI), "DARKKHAKI").color
        val DARKMAGENTA = NamedColor(Color(javafx.scene.paint.Color.DARKMAGENTA), "DARKMAGENTA").color
        val DARKOLIVEGREEN = NamedColor(Color(javafx.scene.paint.Color.DARKOLIVEGREEN), "DARKOLIVEGREEN").color
        val DARKORANGE = NamedColor(Color(javafx.scene.paint.Color.DARKORANGE), "DARKORANGE").color
        val DARKORCHID = NamedColor(Color(javafx.scene.paint.Color.DARKORCHID), "DARKORCHID").color
        val DARKRED = NamedColor(Color(javafx.scene.paint.Color.DARKRED), "DARKRED").color
        val DARKSALMON = NamedColor(Color(javafx.scene.paint.Color.DARKSALMON), "DARKSALMON").color
        val DARKSEAGREEN = NamedColor(Color(javafx.scene.paint.Color.DARKSEAGREEN), "DARKSEAGREEN").color
        val DARKSLATEBLUE = NamedColor(Color(javafx.scene.paint.Color.DARKSLATEBLUE), "DARKSLATEBLUE").color
        val DARKSLATEGRAY = NamedColor(Color(javafx.scene.paint.Color.DARKSLATEGRAY), "DARKSLATEGRAY").color
        val DARKTURQUOISE = NamedColor(Color(javafx.scene.paint.Color.DARKTURQUOISE), "DARKTURQUOISE").color
        val DARKVIOLET = NamedColor(Color(javafx.scene.paint.Color.DARKVIOLET), "DARKVIOLET").color
        val DEEPPINK = NamedColor(Color(javafx.scene.paint.Color.DEEPPINK), "DEEPPINK").color
        val DEEPSKYBLUE = NamedColor(Color(javafx.scene.paint.Color.DEEPSKYBLUE), "DEEPSKYBLUE").color
        val DIMGRAY = NamedColor(Color(javafx.scene.paint.Color.DIMGRAY), "DIMGRAY").color
        val DODGERBLUE = NamedColor(Color(javafx.scene.paint.Color.DODGERBLUE), "DODGERBLUE").color
        val FIREBRICK = NamedColor(Color(javafx.scene.paint.Color.FIREBRICK), "FIREBRICK").color
        val FLORALWHITE = NamedColor(Color(javafx.scene.paint.Color.FLORALWHITE), "FLORALWHITE").color
        val FORESTGREEN = NamedColor(Color(javafx.scene.paint.Color.FORESTGREEN), "FORESTGREEN").color
        val FUCHSIA = NamedColor(Color(javafx.scene.paint.Color.FUCHSIA), "FUCHSIA").color
        val GAINSBORO = NamedColor(Color(javafx.scene.paint.Color.GAINSBORO), "GAINSBORO").color
        val GHOSTWHITE = NamedColor(Color(javafx.scene.paint.Color.GHOSTWHITE), "GHOSTWHITE").color
        val GOLD = NamedColor(Color(javafx.scene.paint.Color.GOLD), "GOLD").color
        val GOLDENROD = NamedColor(Color(javafx.scene.paint.Color.GOLDENROD), "GOLDENROD").color
        val GRAY = NamedColor(Color(javafx.scene.paint.Color.GRAY), "GRAY").color
        val GREEN = NamedColor(Color(javafx.scene.paint.Color.GREEN), "GREEN").color
        val GREENYELLOW = NamedColor(Color(javafx.scene.paint.Color.GREENYELLOW), "GREENYELLOW").color
        val HONEYDEW = NamedColor(Color(javafx.scene.paint.Color.HONEYDEW), "HONEYDEW").color
        val HOTPINK = NamedColor(Color(javafx.scene.paint.Color.HOTPINK), "HOTPINK").color
        val INDIANRED = NamedColor(Color(javafx.scene.paint.Color.INDIANRED), "INDIANRED").color
        val INDIGO = NamedColor(Color(javafx.scene.paint.Color.INDIGO), "INDIGO").color
        val IVORY = NamedColor(Color(javafx.scene.paint.Color.IVORY), "IVORY").color
        val KHAKI = NamedColor(Color(javafx.scene.paint.Color.KHAKI), "KHAKI").color
        val LAVENDER = NamedColor(Color(javafx.scene.paint.Color.LAVENDER), "LAVENDER").color
        val LAVENDERBLUSH = NamedColor(Color(javafx.scene.paint.Color.LAVENDERBLUSH), "LAVENDERBLUSH").color
        val LAWNGREEN = NamedColor(Color(javafx.scene.paint.Color.LAWNGREEN), "LAWNGREEN").color
        val LEMONCHIFFON = NamedColor(Color(javafx.scene.paint.Color.LEMONCHIFFON), "LEMONCHIFFON").color
        val LIGHTBLUE = NamedColor(Color(javafx.scene.paint.Color.LIGHTBLUE), "LIGHTBLUE").color
        val LIGHTCORAL = NamedColor(Color(javafx.scene.paint.Color.LIGHTCORAL), "LIGHTCORAL").color
        val LIGHTCYAN = NamedColor(Color(javafx.scene.paint.Color.LIGHTCYAN), "LIGHTCYAN").color
        val LIGHTGOLDENRODYELLOW = NamedColor(Color(javafx.scene.paint.Color.LIGHTGOLDENRODYELLOW), "LIGHTGOLDENRODYELLOW").color
        val LIGHTGRAY = NamedColor(Color(javafx.scene.paint.Color.LIGHTGRAY), "LIGHTGRAY").color
        val LIGHTGREEN = NamedColor(Color(javafx.scene.paint.Color.LIGHTGREEN), "LIGHTGREEN").color
        val LIGHTGREY = NamedColor(Color(javafx.scene.paint.Color.LIGHTGREY), "LIGHTGREY").color
        val LIGHTPINK = NamedColor(Color(javafx.scene.paint.Color.LIGHTPINK), "LIGHTPINK").color
        val LIGHTSALMON = NamedColor(Color(javafx.scene.paint.Color.LIGHTSALMON), "LIGHTSALMON").color
        val LIGHTSEAGREEN = NamedColor(Color(javafx.scene.paint.Color.LIGHTSEAGREEN), "LIGHTSEAGREEN").color
        val LIGHTSKYBLUE = NamedColor(Color(javafx.scene.paint.Color.LIGHTSKYBLUE), "LIGHTSKYBLUE").color
        val LIGHTSLATEGRAY = NamedColor(Color(javafx.scene.paint.Color.LIGHTSLATEGRAY), "LIGHTSLATEGRAY").color
        val LIGHTSTEELBLUE = NamedColor(Color(javafx.scene.paint.Color.LIGHTSTEELBLUE), "LIGHTSTEELBLUE").color
        val LIGHTYELLOW = NamedColor(Color(javafx.scene.paint.Color.LIGHTYELLOW), "LIGHTYELLOW").color
        val LIME = NamedColor(Color(javafx.scene.paint.Color.LIME), "LIME").color
        val LIMEGREEN = NamedColor(Color(javafx.scene.paint.Color.LIMEGREEN), "LIMEGREEN").color
        val LINEN = NamedColor(Color(javafx.scene.paint.Color.LINEN), "LINEN").color
        val MAGENTA = NamedColor(Color(javafx.scene.paint.Color.MAGENTA), "MAGENTA").color
        val MAROON = NamedColor(Color(javafx.scene.paint.Color.MAROON), "MAROON").color
        val MEDIUMAQUAMARINE = NamedColor(Color(javafx.scene.paint.Color.MEDIUMAQUAMARINE), "MEDIUMAQUAMARINE").color
        val MEDIUMBLUE = NamedColor(Color(javafx.scene.paint.Color.MEDIUMBLUE), "MEDIUMBLUE").color
        val MEDIUMORCHID = NamedColor(Color(javafx.scene.paint.Color.MEDIUMORCHID), "MEDIUMORCHID").color
        val MEDIUMPURPLE = NamedColor(Color(javafx.scene.paint.Color.MEDIUMPURPLE), "MEDIUMPURPLE").color
        val MEDIUMSEAGREEN = NamedColor(Color(javafx.scene.paint.Color.MEDIUMSEAGREEN), "MEDIUMSEAGREEN").color
        val MEDIUMSLATEBLUE = NamedColor(Color(javafx.scene.paint.Color.MEDIUMSLATEBLUE), "MEDIUMSLATEBLUE").color
        val MEDIUMSPRINGGREEN = NamedColor(Color(javafx.scene.paint.Color.MEDIUMSPRINGGREEN), "MEDIUMSPRINGGREEN").color
        val MEDIUMTURQUOISE = NamedColor(Color(javafx.scene.paint.Color.MEDIUMTURQUOISE), "MEDIUMTURQUOISE").color
        val MEDIUMVIOLETRED = NamedColor(Color(javafx.scene.paint.Color.MEDIUMVIOLETRED), "MEDIUMVIOLETRED").color
        val MIDNIGHTBLUE = NamedColor(Color(javafx.scene.paint.Color.MIDNIGHTBLUE), "MIDNIGHTBLUE").color
        val MINTCREAM = NamedColor(Color(javafx.scene.paint.Color.MINTCREAM), "MINTCREAM").color
        val MISTYROSE = NamedColor(Color(javafx.scene.paint.Color.MISTYROSE), "MISTYROSE").color
        val MOCCASIN = NamedColor(Color(javafx.scene.paint.Color.MOCCASIN), "MOCCASIN").color
        val NAVAJOWHITE = NamedColor(Color(javafx.scene.paint.Color.NAVAJOWHITE), "NAVAJOWHITE").color
        val NAVY = NamedColor(Color(javafx.scene.paint.Color.NAVY), "NAVY").color
        val OLDLACE = NamedColor(Color(javafx.scene.paint.Color.OLDLACE), "OLDLACE").color
        val OLIVE = NamedColor(Color(javafx.scene.paint.Color.OLIVE), "OLIVE").color
        val OLIVEDRAB = NamedColor(Color(javafx.scene.paint.Color.OLIVEDRAB), "OLIVEDRAB").color
        val ORANGE = NamedColor(Color(javafx.scene.paint.Color.ORANGE), "ORANGE").color
        val ORANGERED = NamedColor(Color(javafx.scene.paint.Color.ORANGERED), "ORANGERED").color
        val ORCHID = NamedColor(Color(javafx.scene.paint.Color.ORCHID), "ORCHID").color
        val PALEGOLDENROD = NamedColor(Color(javafx.scene.paint.Color.PALEGOLDENROD), "PALEGOLDENROD").color
        val PALEGREEN = NamedColor(Color(javafx.scene.paint.Color.PALEGREEN), "PALEGREEN").color
        val PALETURQUOISE = NamedColor(Color(javafx.scene.paint.Color.PALETURQUOISE), "PALETURQUOISE").color
        val PALEVIOLETRED = NamedColor(Color(javafx.scene.paint.Color.PALEVIOLETRED), "PALEVIOLETRED").color
        val PAPAYAWHIP = NamedColor(Color(javafx.scene.paint.Color.PAPAYAWHIP), "PAPAYAWHIP").color
        val PEACHPUFF = NamedColor(Color(javafx.scene.paint.Color.PEACHPUFF), "PEACHPUFF").color
        val PERU = NamedColor(Color(javafx.scene.paint.Color.PERU), "PERU").color
        val PINK = NamedColor(Color(javafx.scene.paint.Color.PINK), "PINK").color
        val PLUM = NamedColor(Color(javafx.scene.paint.Color.PLUM), "PLUM").color
        val POWDERBLUE = NamedColor(Color(javafx.scene.paint.Color.POWDERBLUE), "POWDERBLUE").color
        val PURPLE = NamedColor(Color(javafx.scene.paint.Color.PURPLE), "PURPLE").color
        val RED = NamedColor(Color(javafx.scene.paint.Color.RED), "RED").color
        val ROSYBROWN = NamedColor(Color(javafx.scene.paint.Color.ROSYBROWN), "ROSYBROWN").color
        val ROYALBLUE = NamedColor(Color(javafx.scene.paint.Color.ROYALBLUE), "ROYALBLUE").color
        val SADDLEBROWN = NamedColor(Color(javafx.scene.paint.Color.SADDLEBROWN), "SADDLEBROWN").color
        val SALMON = NamedColor(Color(javafx.scene.paint.Color.SALMON), "SALMON").color
        val SANDYBROWN = NamedColor(Color(javafx.scene.paint.Color.SANDYBROWN), "SANDYBROWN").color
        val SEAGREEN = NamedColor(Color(javafx.scene.paint.Color.SEAGREEN), "SEAGREEN").color
        val SEASHELL = NamedColor(Color(javafx.scene.paint.Color.SEASHELL), "SEASHELL").color
        val SIENNA = NamedColor(Color(javafx.scene.paint.Color.SIENNA), "SIENNA").color
        val SILVER = NamedColor(Color(javafx.scene.paint.Color.SILVER), "SILVER").color
        val SKYBLUE = NamedColor(Color(javafx.scene.paint.Color.SKYBLUE), "SKYBLUE").color
        val SLATEBLUE = NamedColor(Color(javafx.scene.paint.Color.SLATEBLUE), "SLATEBLUE").color
        val SLATEGRAY = NamedColor(Color(javafx.scene.paint.Color.SLATEGRAY), "SLATEGRAY").color
        val SNOW = NamedColor(Color(javafx.scene.paint.Color.SNOW), "SNOW").color
        val SPRINGGREEN = NamedColor(Color(javafx.scene.paint.Color.SPRINGGREEN), "SPRINGGREEN").color
        val STEELBLUE = NamedColor(Color(javafx.scene.paint.Color.STEELBLUE), "STEELBLUE").color
        val TAN = NamedColor(Color(javafx.scene.paint.Color.TAN), "TAN").color
        val TEAL = NamedColor(Color(javafx.scene.paint.Color.TEAL), "TEAL").color
        val THISTLE = NamedColor(Color(javafx.scene.paint.Color.THISTLE), "THISTLE").color
        val TOMATO = NamedColor(Color(javafx.scene.paint.Color.TOMATO), "TOMATO").color
        val TURQUOISE = NamedColor(Color(javafx.scene.paint.Color.TURQUOISE), "TURQUOISE").color
        val VIOLET = NamedColor(Color(javafx.scene.paint.Color.VIOLET), "VIOLET").color
        val WHEAT = NamedColor(Color(javafx.scene.paint.Color.WHEAT), "WHEAT").color
        val WHITE = NamedColor(Color(javafx.scene.paint.Color.WHITE), "WHITE").color
        val WHITESMOKE = NamedColor(Color(javafx.scene.paint.Color.WHITESMOKE), "WHITESMOKE").color
        val YELLOW = NamedColor(Color(javafx.scene.paint.Color.YELLOW), "YELLOW").color
        val YELLOWGREEN = NamedColor(Color(javafx.scene.paint.Color.YELLOWGREEN), "YELLOWGREEN").color

        @JvmStatic
        fun valueOf(value: String): Color {
            val namedColor = NamedColor.get(value.trim().uppercase(Locale.ENGLISH))
            namedColor?.let {
                return it
            }
            return Color(value)
        }
    }
}