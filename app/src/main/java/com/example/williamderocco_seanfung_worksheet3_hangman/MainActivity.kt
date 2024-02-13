package com.example.williamderocco_seanfung_worksheet3_hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val keyboardRow1: LinearLayout = findViewById(R.id.keyboardRow1)
        val keyboardRow2: LinearLayout = findViewById(R.id.keyboardRow2)
        val keyboardRow3: LinearLayout = findViewById(R.id.keyboardRow3)
        val keyboardRow4: LinearLayout = findViewById(R.id.keyboardRow4)

        val alphabet = "abcdefghijklmnopqrstuvwxyz"

        // Loop to create buttons for each letter 'a' to 'z'
        for ((index, char) in alphabet.withIndex()) {
            val button = Button(this)
            button.text = char.toString()
            button.layoutParams = LinearLayout.LayoutParams(
                100,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            button.setOnClickListener {
                // Handle button click event
            }

            // Determine which row to add the button to
            if (index < 8) {
                keyboardRow1.addView(button)
            } else if (index < 16) {
                keyboardRow2.addView(button)
            } else if (index < 24) {
                keyboardRow3.addView(button)
            } else {
                keyboardRow4.addView(button)
            }
        }
    }
}