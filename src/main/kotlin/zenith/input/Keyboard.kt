package zenith.input

import javafx.event.EventHandler
import javafx.scene.input.KeyEvent
import zenith.core.Game
import java.util.*

object Keyboard {
    private val newPressedKeys = mutableSetOf<Key>()
    private val newReleasedKeys = mutableSetOf<Key>()
    private var newTypedString = ""
    private val _downKeys = mutableSetOf<Key>()
    private val _upKeys = mutableSetOf<Key>()
    private val _pressedKeys = mutableSetOf<Key>()
    private val _releasedKeys = mutableSetOf<Key>()
    private var _typedString = ""
    val typedString get() = _typedString
    val downKeys: Set<Key> get() = Collections.unmodifiableSet(_downKeys)
    val upKeys: Set<Key> get() = Collections.unmodifiableSet(_upKeys)
    val pressedKeys: Set<Key> get() = Collections.unmodifiableSet(_pressedKeys)
    val releasedKeys: Set<Key> get() = Collections.unmodifiableSet(_releasedKeys)

    init {
        Game.throwIfUninitialized()
        Game.fxStage.scene.onKeyTyped = EventHandler { onKeyTyped(it) }
        Game.fxStage.scene.onKeyPressed = EventHandler { onKeyPressed(it) }
        Game.fxStage.scene.onKeyReleased = EventHandler { onKeyReleased(it) }
    }

    fun isKeyDown(key: Key): Boolean {
        return _downKeys.contains(key)
    }

    fun isKeyUp(key: Key): Boolean {
        return _upKeys.contains(key)
    }

    fun isKeyPressed(key: Key): Boolean {
        return _pressedKeys.contains(key)
    }

    fun isKeyReleased(key: Key): Boolean {
        return _releasedKeys.contains(key)
    }

    internal fun update() {
        if (!Game.focused) {
            reset()
            return
        }
        updateTypedString()
        updatePressedKeys()
        updateDownKeys()
        updateReleasedKeys()
        updateUpKeys()
    }

    private fun onKeyTyped(event: KeyEvent) {
        if (event.character.isEmpty()) {
            return
        }
        val typedChar = event.character.first()
        if (isCharTypedValid(typedChar)) {
            newTypedString += typedChar
        }
    }

    private fun onKeyPressed(event: KeyEvent) {
        newPressedKeys.add(Key.get(event.code))
    }

    private fun onKeyReleased(event: KeyEvent) {
        newReleasedKeys.add(Key.get(event.code))
    }

    private fun updateTypedString() {
        _typedString = newTypedString
        newTypedString = ""
    }

    private fun updateDownKeys() {
        _downKeys.addAll(newPressedKeys)
        newPressedKeys.clear()
    }

    private fun updateUpKeys() {
        _upKeys.clear()
        for (key in Key.entries) {
            if (!_downKeys.contains(key)) {
                _upKeys.add(key)
            }
        }
    }

    private fun updatePressedKeys() {
        _pressedKeys.clear()
        _pressedKeys.addAll(newPressedKeys)
        _pressedKeys.removeAll(_downKeys)
    }

    private fun updateReleasedKeys() {
        _releasedKeys.clear()
        _releasedKeys.addAll(newReleasedKeys)
        newReleasedKeys.clear()
        _downKeys.removeAll(_releasedKeys)
    }

    private fun reset() {
        newTypedString = ""
        _typedString = ""
        newPressedKeys.clear()
        _downKeys.clear()
        newReleasedKeys.clear()
        _releasedKeys.clear()
        updateUpKeys()
    }

    private fun isCharTypedValid(charTyped: Char): Boolean {
        return (charTyped in ' '..'~')
    }
}