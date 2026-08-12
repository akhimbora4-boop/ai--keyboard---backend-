package com.asim.keyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class KeyboardService : InputMethodService() {

    override fun onCreateInputView(): View {

        val keyboard = LinearLayout(this)
        keyboard.orientation = LinearLayout.VERTICAL
        keyboard.setPadding(8, 8, 8, 8)

        val rows = listOf(
            listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
            listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
            listOf("⇧", "Z", "X", "C", "V", "B", "N", "M", "⌫"),
            listOf("123", "😊", "Space", "Enter")
        )

        for (row in rows) {

            val rowLayout = LinearLayout(this)
            rowLayout.orientation = LinearLayout.HORIZONTAL

            for (key in row) {

                val button = Button(this)
                button.text = key

                rowLayout.addView(
                    button,
                    LinearLayout.LayoutParams(
                        0,
                        60,
                        1f
                    )
                )

                button.setOnClickListener {

                    val connection = currentInputConnection

                    when (key) {

                        "⌫" -> {
                            connection.deleteSurroundingText(1, 0)
                        }

                        "Space" -> {
                            connection.commitText(" ", 1)
                        }

                        "Enter" -> {
                            connection.sendKeyEvent(
                                android.view.KeyEvent(
                                    android.view.KeyEvent.ACTION_DOWN,
                                    android.view.KeyEvent.KEYCODE_ENTER
                                )
                            )
                        }

                        "😊" -> {
                            connection.commitText("😊", 1)
                        }

                        "123" -> {
                            connection.commitText("123", 1)
                        }

                        "⇧" -> {
                            connection.commitText("", 1)
                        }

                        else -> {
                            connection.commitText(key, 1)
                        }
                    }
                }
            }

            keyboard.addView(rowLayout)
        }

        return keyboard
    }
}