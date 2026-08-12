package com.asim.keyboard

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.media.AudioManager
import android.view.View
import android.view.inputmethod.InputConnection

class KeyboardService : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private lateinit var keyboardView: KeyboardView
    private lateinit var keyboard: Keyboard
    private var caps = false

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as KeyboardView
        keyboard = Keyboard(this, R.xml.keyboard)
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)
        return keyboardView
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        val inputConnection: InputConnection = currentInputConnection
        playClick(primaryCode)

        when (primaryCode) {
            -1 -> { // SHIFT
                caps = !caps
                keyboard.isShifted = caps
                keyboardView.invalidateAllKeys()
            }
            -5 -> { // BACKSPACE
                inputConnection.deleteSurroundingText(1, 0)
            }
            -2 -> { // SYMBOLS - ইয়াত আন keyboard layout load কৰিব পাৰি
                // TODO: symbols keyboard
            }
            32 -> { // SPACE
                inputConnection.commitText(" ", 1)
            }
            10 -> { // ENTER
                inputConnection.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_ENTER))
            }
            else -> {
                var code = primaryCode
                if (caps) code = Character.toUpperCase(code)
                inputConnection.commitText(code.toChar().toString(), 1)
            }
        }
    }

    private fun playClick(keyCode: Int) {
        val am = getSystemService(AUDIO_SERVICE) as AudioManager
        when (keyCode) {
            32 -> am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR)
            10 -> am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN)
            -5 -> am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE)
            else -> am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD)
        }
    }

    // বাকী function বোৰ খালী ৰাখা
    override fun onPress(primaryCode: Int) {}
    override fun onRelease(primaryCode: Int) {}
    override fun onText(text: CharSequence?) {}
    override fun swipeLeft() {}
    override fun swipeRight() {}
    override fun swipeDown() {}
    override fun swipeUp() {}
}