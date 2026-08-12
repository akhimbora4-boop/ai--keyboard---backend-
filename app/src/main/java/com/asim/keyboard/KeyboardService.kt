package com.asim.keyboard

import android.inputmethodservice.InputMethodService
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class KeyboardService : InputMethodService() {

    override fun onCreateInputView(): View {

        val keyboard = LinearLayout(this)
        keyboard.orientation = LinearLayout.VERTICAL
        keyboard.setPadding(6, 6, 6, 6)
        keyboard.setBackgroundColor(Color.rgb(25, 25, 25))

        val rows = listOf(
            listOf("Q","W","E","R","T","Y","U","I","O","P"),
            listOf("A","S","D","F","G","H","J","K","L"),
            listOf("⇧","Z","X","C","V","B","N","M","⌫"),
            listOf("123","😊","Space","Enter")
        )

        for (row in rows) {

            val rowLayout = LinearLayout(this)
            rowLayout.orientation = LinearLayout.HORIZONTAL
            rowLayout.gravity = Gravity.CENTER

            for (key in row) {

                val button = Button(this)
                button.text = key
                button.textSize = 15f

                val params = LinearLayout.LayoutParams(
                    0,
                    55,
                    1f
                )

                params.setMargins(2, 2, 2, 2)

                rowLayout.addView(button, params)

                button.setOnClickListener {

                    val connection = currentInputConnection
                        ?: return@setOnClickListener

                    when (key) {

                        "⌫" -> {
                            connection.deleteSurroundingText(1, 0)
                        }

                        "Space" -> {
                            connection.commitText(" ", 1)
                        }

                        "Enter" -> {
                            connection.commitText("\n", 1)
                        }

                        "😊" -> {
                            connection.commitText("😊", 1)
                        }

                        "123" -> {
                            connection.commitText("123", 1)
                        }

                        "⇧" -> {
                            // Shift will be added next
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