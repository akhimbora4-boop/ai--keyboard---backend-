package com.asim.keyboard

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(30, 40, 30, 30)

        val title = TextView(this)
        title.text = "English Keyboard"
        title.textSize = 28f

        val button = Button(this)
        button.text = "Enable Keyboard"

        button.setOnClickListener {
            val imm = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager

            imm.showInputMethodPicker()
        }

        layout.addView(title)
        layout.addView(button)

        setContentView(layout)
    }
}