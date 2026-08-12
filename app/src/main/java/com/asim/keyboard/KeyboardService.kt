package com.asim.keyboard

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputConnection

class KeyboardService : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private lateinit var keyboardView: KeyboardView
    private lateinit var keyboard: Keyboard // QWERTY
    private lateinit var symbolsKeyboard: Keyboard // 123
    private var isSymbols = false

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as KeyboardView

        keyboard = Keyboard(this, R.xml.keyboard)
        symbolsKeyboard = Keyboard(this, R.xml.symbols)

        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)
        return keyboardView
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        val ic: InputConnection? = currentInputConnection
        if (ic == null) return

        when (primaryCode) {
            -1 -> { // SHIFT
            }
            -2 -> { // 123 / ABC button
                isSymbols = !isSymbols
                if (isSymbols) {
                    keyboardView.keyboard = symbolsKeyboard
                } else {
                    keyboardView.keyboard = keyboard
                }
            }
            -3 -> { // Emoji
            }
            -4 -> { // =/< button
            }
            -5 -> { // DELETE
                ic.deleteSurroundingText(1, 0)
            }
            10 -> { // ENTER
                ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            }
            32 -> { // SPACE
                ic.commitText(" ", 1)
            }
            else -> { // Normal letter/number
                val code = primaryCode.toChar()
                ic.commitText(code.toString(), 1)
            }
        }
    }

    override fun onPress(primaryCode: Int) {}
    override fun onRelease(primaryCode: Int) {}
    override fun onText(text: CharSequence?) {}
    override fun swipeLeft() {}
    override fun swipeRight() {}
    override fun swipeDown() {}
    override fun swipeUp() {}
}