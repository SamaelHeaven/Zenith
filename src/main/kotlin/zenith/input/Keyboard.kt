package zenith.input

import javafx.scene.input.KeyEvent
import zenith.core.Game
import java.util.Collections

object Keyboard {
    private var newTypedString = ""
    private val newPressedKeys = HashSet<Key>()
    private val newReleasedKeys = HashSet<Key>()
    private var _typedString = ""
    private val _downKeys = HashSet<Key>()
    private val _upKeys = HashSet<Key>()
    private val releasedKeys = HashSet<Key>()

    val typedString get() = _typedString

    val downKeys: Set<Key> get() = Collections.unmodifiableSet(_downKeys)

    val upKeys: Set<Key> get() = Collections.unmodifiableSet(_upKeys)

    val pressedKeys: Set<Key> get() = Collections.unmodifiableSet(releasedKeys)

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
        return releasedKeys.contains(key)
    }

    internal fun update() {
        if (!Game.focused) {
            reset()
            return
        }
        updateTypedString()
        updateDownKeys()
        updateReleasedKeys()
        updateUpKeys()
    }

    internal fun onKeyTyped(event: KeyEvent) {
        newTypedString += event.text
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

    private fun updateReleasedKeys() {
        releasedKeys.clear()
        releasedKeys.addAll(newReleasedKeys)
        newReleasedKeys.clear()
        _downKeys.removeAll(releasedKeys)
    }

    private fun reset() {
        newTypedString = ""
        _typedString = ""
        newPressedKeys.clear()
        _downKeys.clear()
        newReleasedKeys.clear()
        releasedKeys.clear()
        updateUpKeys()
    }
}