package zenith.input

import javafx.scene.input.KeyEvent
import zenith.core.Game
import java.util.*

object Keyboard {
    private var newTypedString = ""
    private val newPressedKeys = HashSet<Key>()
    private val newReleasedKeys = HashSet<Key>()
    private var _typedString = ""
    private val _downKeys = HashSet<Key>()
    private val _upKeys = HashSet<Key>()
    private var _pressedKeys = HashSet<Key>()
    private val _releasedKeys = HashSet<Key>()

    val typedString get() = _typedString

    val downKeys: Set<Key> get() = Collections.unmodifiableSet(_downKeys)

    val upKeys: Set<Key> get() = Collections.unmodifiableSet(_upKeys)

    val pressedKeys: Set<Key> get() = Collections.unmodifiableSet(_pressedKeys)

    val releasedKeys: Set<Key> get() = Collections.unmodifiableSet(_releasedKeys)

    init {
        Game.throwIfUninitialized()
    }

    fun isKeyUp(key: Key): Boolean {
        return _upKeys.contains(key)
    }

    fun isKeyDown(key: Key): Boolean {
        return _downKeys.contains(key)
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

    internal fun onKeyTyped(event: KeyEvent) {
        if (event.character.isEmpty()) {
            return
        }
        val typedChar = event.character.first()
        println(typedChar)
        println(isCharTypedValid(typedChar))
        println(typedChar.code)
        if (isCharTypedValid(typedChar)) {
            newTypedString += typedString
            println(newTypedString)
        }
    }

    internal fun onKeyPressed(event: KeyEvent) {
        newPressedKeys.add(Key.get(event.code))
    }

    internal fun onKeyReleased(event: KeyEvent) {
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
        return (charTyped.code in ' '.code..'~'.code)
    }
}