package com.example.williamderocco_seanfung_worksheet3_hangman

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer


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
        val winOrLoseTextView : TextView = findViewById(R.id.winOrLose)
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {//Used web to find the orientation function.
            // Landscape mode
            // Show hint button
            hintButton.visibility = View.VISIBLE // or View.GONE to hide it
        } else {
            // Portrait mode
            // Hide the hint button
            hintButton.visibility = View.GONE
        }
        hangmanViewModel.gameOutcomeLiveData.observe(this) { outcome ->
            when (outcome) {//Used CHAT GPT for help with this. This determines what the text will be if there is a winner
                GameOutcome.WIN -> winGame(hangmanViewModel.answer)
                GameOutcome.LOSS -> loseGame(hangmanViewModel.answer)
            }
        }
        hangmanViewModel.winOrLoseTextLiveData.observe(this, Observer { text ->
            winOrLoseTextView.text = text
        })
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
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        hangmanViewModel.saveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        hangmanViewModel.restoreInstanceState(savedInstanceState)
    }


    private fun winGame(word: String){
        val winOrLoseTextView : TextView = findViewById(R.id.winOrLose)
        winOrLoseTextView.text = "YOU WIN"
    }
    private fun loseGame(word: String){
        val answerTextView: TextView = findViewById(R.id.answerTextView)
        answerTextView.text = word
        val winOrLoseTextView : TextView = findViewById(R.id.winOrLose)
        winOrLoseTextView.text = "YOU LOSE"
    }

}
