package com.example.williamderocco_seanfung_worksheet3_hangman

import android.content.Context
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModel

private const val TAG = "hangmanView"
class HangmanViewModel : ViewModel() {
    var answer = ""
    var hint = ""
    var usedLetters = mutableListOf<String>()
    private var numHints = 0
    private var maxTries = 6
    var currentTries = 0
    lateinit var underscoredLetters: String
    var hangman: Int = R.drawable.state0
    var playing = false
    var win = false
    var play = false
    var firstHintShowed = false


    private val wordDictionary: Map<String, List<String>> = mapOf(
        "country" to listOf("america", "japan", "china", "mexico", "korea"),
        "animal" to listOf("elephant", "zebra", "chicken", "anaconda", "hippo"),
        "fruit" to listOf("apple", "grape", "kiwi", "pear", "orange"),
        "food" to listOf("pizza", "hamburger", "pasta", "burrito", "ramen"),
        "sports" to listOf("basketball", "baseball", "soccer", "football", "hockey")
    )

    fun newGame(){
        val currentKey =  wordDictionary.keys.random()
        val currentWord = wordDictionary[currentKey]!!.random()
        // build underscored word based on currentWord (randomly chosen
        getUnderscores(currentWord)
        answer = currentWord
        hint = currentKey
        numHints = 0
        currentTries = 0
        usedLetters.clear()
        playing = true
        firstHintShowed = false
        hangman = nextHangman()

    }

    private fun getUnderscores(word: String) {
        val sb = StringBuilder()
        word.forEach { _ -> sb.append("_")}
        underscoredLetters = sb.toString()
    }

    fun initializeKeyboardButtons(
        context: Context,
        keyboardRow1: LinearLayout,
        keyboardRow2: LinearLayout,
        keyboardRow3: LinearLayout,
        keyboardRow4: LinearLayout
    ) {
        val alphabet = "abcdefghijklmnopqrstuvwxyz"

        // Loop to create buttons for each letter 'a' to 'z'
        for ((index, char) in alphabet.withIndex()) {
            val button = Button(context)
            button.text = char.toString()
            button.layoutParams = LinearLayout.LayoutParams(
                100,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            button.setOnClickListener {
                this.guess(context, char, false)
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

    private fun guess(context: Context, letter: Char, hint: Boolean) {
        if (playing) {
            usedLetters.add(letter.toString())
            val indexLetter = mutableListOf<Int>()

            answer .forEachIndexed { index, c ->  //c = char
                if (c.equals(letter, ignoreCase = true)){
                    indexLetter.add(index)
                }
            }
            var underscoredWord = underscoredLetters
            indexLetter.forEach { index ->
                val stringBuild = StringBuilder(underscoredWord).also { it.setCharAt(index, letter)}//Used Chat GPT to understand stringBuilder and to use also.
                underscoredWord = stringBuild.toString()
            }

            // case 1: incorrect guess (and no hint)
            if (indexLetter.isEmpty()){
                if(!hint)currentTries++
                hangman = nextHangman()
            }

            // case 2: hit max tries
            if (currentTries == maxTries){
                playing = false
                win = false
            }

            // case 3: guessed correct word
            underscoredLetters = underscoredWord
            if(underscoredLetters.lowercase() == answer){
                playing = false
                win = true
            }
        } else {
            Toast.makeText(context, "Select 'new game' to start!", Toast.LENGTH_SHORT).show()
        }

    }

    fun nextHangman(): Int{//Updates the drawings
        return when (currentTries) {
            0 -> R.drawable.state0
            1 -> R.drawable.state1
            2 -> R.drawable.state2
            3 -> R.drawable.state3
            4 -> R.drawable.state4
            5 -> R.drawable.state5
            6 -> R.drawable.state6
            else -> R.drawable.state6
        }
    }

}