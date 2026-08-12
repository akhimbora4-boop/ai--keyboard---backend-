package com.asim.keyboard

import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.view.Gravity
import android.view.View
import android.widget.TextView

class KeyboardService : InputMethodService() {

    override fun onCreateInputView(): View {

        val text = TextView(this)

        text.text = "ENGLISH KEYBOARD WORKS"
        text.textSize = 22f
        text.setTextColor(Color.WHITE)
        text.setBackgroundColor(Color.DKGRAY)
        text.gravity = Gravity.CENTER
        text.setPadding(20, 40, 20, 40)

        return text
    }
}