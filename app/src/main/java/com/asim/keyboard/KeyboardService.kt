package com.asim.keyboard

import android.content.ClipboardManager
import android.content.Context
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputConnection

class KeyboardService : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private lateinit var keyboardView: KeyboardView
    private lateinit var keyboard: Keyboard
    private lateinit var symbolsKeyboard: Keyboard
    private var isSymbols = false
    private var isCaps = false

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as KeyboardView

        keyboard = Keyboard(this, R.xml.keyboard)
        symbolsKeyboard = Keyboard(this, R.xml.symbols)

        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)
        return keyboardView
    }

    // এই নতুন function টো add কৰিলোঁ - key ৰ letter সলনি কৰাৰ বাবে
    private fun updateShiftState() {
        for (key in keyboard.keys) {
            if (key.label!= null && key.label.length == 1) {
                val letter = key.label[0]
                if (Character.isLetter(letter)) {
                    key.label = if (isCaps) letter.uppercaseChar().toString()
                                else letter.lowercaseChar().toString()
                }
            }
        }
        keyboardView.invalidateAllKeys()
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        val ic: InputConnection? = currentInputConnection
        if (ic == null) return

        when (primaryCode) {
            -1 -> { // SHIFT
                isCaps =!isCaps
                updateShiftState() // ইয়াত key বোৰ refresh হব
            }
            -2 -> { // 123 / ABC button
                isSymbols =!isSymbols
                if (isSymbols) {
                    keyboardView.keyboard = symbolsKeyboard
                } else {
                    keyboardView.keyboard = keyboard
                    updateShiftState() // QWERTY লৈ উভতি আহিলে shift state ঠিক কৰা
                }
            }
            -4 -> { // Clipboard
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = clipboard.primaryClip
                if(clip!= null && clip.itemCount > 0) {
                    val text = clip.getItemAt(0).text
                    ic.commitText(text, 1)
                }
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
                var code = primaryCode.toChar()
                if(isCaps) {
                    code = code.uppercaseChar()
                    // 1টা capital type কৰাৰ পিছত auto small
                    isCaps = false
                    updateShiftState()
                }
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