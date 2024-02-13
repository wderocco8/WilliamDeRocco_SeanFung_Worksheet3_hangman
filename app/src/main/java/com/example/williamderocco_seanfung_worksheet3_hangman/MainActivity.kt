package com.example.williamderocco_seanfung_worksheet3_hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
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


        // initialize keyboard
        hangmanViewModel.initializeKeyboardButtons(this, keyboardRow1, keyboardRow2, keyboardRow3, keyboardRow4)
        // start game (only initialized on initial create)
        hangmanViewModel.newGame()
        // call refreshUI

        newGameButton.setOnClickListener {
            // call refreshUI
            hangmanViewModel.newGame() // Call newGame function when button is clicked
        }
    }



//    private fun refreshUI() {
//        //call hint function
////        if(hangmanViewModel.showFirstHint){
////            firstHintText.text = hangmanViewModel.hint
////        }
//        if(hangmanViewModel.playing){
//            answerTextView.text = hangmanViewModel.underscoreWord
//            hangmanImage.setImageDrawable(ContextCompat.getDrawable(this, hangmanViewModel.drawable))
//        }else{
//            if(hangmanViewModel.hasWon){
//                if(!hangmanViewModel.firstPlay) showGameWon(hangmanViewModel.answer)
//            }else{
//                if(!hangmanViewModel.firstPlay)showGameLost(hangmanViewModel.answer)
//            }
//        }
//    }

}
