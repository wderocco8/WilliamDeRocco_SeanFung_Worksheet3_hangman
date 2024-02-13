package com.example.williamderocco_seanfung_worksheet3_hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    // initialize set of letters which have been guessed
    private val guessedLetters = mutableSetOf<Char>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val keyboardRow1: LinearLayout = findViewById(R.id.keyboardRow1)
        val keyboardRow2: LinearLayout = findViewById(R.id.keyboardRow2)
        val keyboardRow3: LinearLayout = findViewById(R.id.keyboardRow3)
        val keyboardRow4: LinearLayout = findViewById(R.id.keyboardRow4)

        initializeKeyboardButtons(keyboardRow1, keyboardRow2, keyboardRow3, keyboardRow4)
    }

    private fun initializeKeyboardButtons(
        keyboardRow1: LinearLayout,
        keyboardRow2: LinearLayout,
        keyboardRow3: LinearLayout,
        keyboardRow4: LinearLayout
    ) {
        val alphabet = "abcdefghijklmnopqrstuvwxyz"

        // Loop to create buttons for each letter 'a' to 'z'
        for ((index, char) in alphabet.withIndex()) {
            val button = Button(this)
            button.text = char.toString()
            button.layoutParams = LinearLayout.LayoutParams(
                100,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            button.setOnClickListener {
                guessLetter(char)
            }

            // Determine which row to add the button to
            when {
                index < 8 -> keyboardRow1.addView(button)
                index < 16 -> keyboardRow2.addView(button)
                index < 23 -> keyboardRow3.addView(button)
                else -> keyboardRow4.addView(button)
            }
        }
    }

    private fun guessLetter(letter: Char) {
        if (guessedLetters.contains(letter)) {
            // Letter has already been guessed
            // Implement your logic here, such as showing a toast or changing the button appearance
            return
        }

        // Add the letter to the set of guessed letters
        guessedLetters.add(letter)

        // Implement further logic for handling the guessed letter, such as checking if it's correct or not
    }


    // reset function

    // hint function




}
