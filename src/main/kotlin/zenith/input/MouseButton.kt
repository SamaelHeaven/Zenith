package zenith.input

enum class MouseButton(private val fxButton: javafx.scene.input.MouseButton) {
    PRIMARY(javafx.scene.input.MouseButton.PRIMARY),
    MIDDLE(javafx.scene.input.MouseButton.MIDDLE),
    SECONDARY(javafx.scene.input.MouseButton.SECONDARY),
    BACK(javafx.scene.input.MouseButton.BACK),
    FORWARD(javafx.scene.input.MouseButton.FORWARD),
    NONE(javafx.scene.input.MouseButton.NONE);

    companion object {
        private val buttons = mutableMapOf<javafx.scene.input.MouseButton, MouseButton>()

        init {
            for (button in MouseButton.entries) {
                buttons[button.fxButton] = button
            }
        }

        internal fun get(fxButton: javafx.scene.input.MouseButton): MouseButton {
            return buttons[fxButton] ?: NONE
        }
    }
}