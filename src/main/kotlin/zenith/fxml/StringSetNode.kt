package zenith.fxml

import java.util.*

class StringSetNode : SetNode<String>() {
    companion object {
        @JvmStatic
        fun valueOf(value: String): StringSetNode {
            val result = StringSetNode()
            val scanner = Scanner(value)
            while (scanner.hasNext()) {
                result.add(scanner.next())
            }
            return result
        }
    }
}