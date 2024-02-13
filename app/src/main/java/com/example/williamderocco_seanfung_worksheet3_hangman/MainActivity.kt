package com.example.williamderocco_seanfung_worksheet3_hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels


class MainActivity : AppCompatActivity() {

    private val hangmanViewModel: HangmanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // obtain all elements from views
        val keyboardRow1: LinearLayout = findViewById(R.id.keyboardRow1)
        val keyboardRow2: LinearLayout = findViewById(R.id.keyboardRow2)
        val keyboardRow3: LinearLayout = findViewById(R.id.keyboardRow3)
        val keyboardRow4: LinearLayout = findViewById(R.id.keyboardRow4)
        val newGameButton: Button = findViewById(R.id.newGame)
        val hintButton: Button = findViewById(R.id.hint)
        val hangmanImageView: ImageView = findViewById(R.id.hangmanImageView)
        val answerTextView: TextView = findViewById(R.id.answerTextView)

        // Observe LiveData and update UI (chatGPT helped with this)
        hangmanViewModel.underscoredLettersLiveData.observe(this) { underscoredLetters ->
            answerTextView.text = underscoredLetters
        }

        hangmanViewModel.hangmanImageLiveData.observe(this) { hangmanImage ->
            hangmanImageView.setImageResource(hangmanImage)
        }

        // initialize keyboard
        hangmanViewModel.initializeKeyboardButtons(this, keyboardRow1, keyboardRow2, keyboardRow3, keyboardRow4)
        // start game (only initialized on initial create)
        hangmanViewModel.newGame()

        newGameButton.setOnClickListener {
            hangmanViewModel.newGame() // Call newGame function when button is clicked
        }
        hintButton.setOnClickListener {
            hangmanViewModel.hint() // Call newGame function when button is clicked
        }
    }

}
