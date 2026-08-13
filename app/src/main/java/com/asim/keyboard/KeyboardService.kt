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

    private fun handleShift() {
        isCaps =!isCaps
        keyboard.isShifted = isCaps // key ৰ background change ৰ বাবে
        
        // প্রতিটো key ৰ label নিজে change কৰা
        val keys = keyboard.keys
        for (key in keys) {
            if (key.label!= null && key.label.length == 1 && Character.isLetter(key.label[0])) {
                key.label = if (isCaps) key.label.toString().uppercase() else key.label.toString().lowercase()
            }
        }
        keyboardView.invalidateAllKeys() // UI refresh
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        val ic: InputConnection? = currentInputConnection
        if (ic == null) return

        when (primaryCode) {
            -1 -> { // SHIFT
                handleShift()
            }
            -2 -> { // 123 / ABC button
                isSymbols =!isSymbols
                if (isSymbols) {
                    keyboardView.keyboard = symbolsKeyboard
                } else {
                    keyboardView.keyboard = keyboard
                    if(isCaps) handleShift() // QWERTY লৈ আহিলে shift ঠিক কৰা
                    handleShift() // 2বাৰ call কৰি আকৌ আগৰ state লৈ অনা
                    handleShift()
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
                ic.commitText(primaryCode.toChar().toString(), 1)
                // 1টা letter type কৰাৰ পিছত shift off
                if(isCaps) {
                    handleShift()
                }
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