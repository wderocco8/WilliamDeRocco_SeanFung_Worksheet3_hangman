package com.example.williamderocco_seanfung_worksheet3_hangman

import android.content.Context
import android.widget.Button
import android.widget.LinearLayout
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

    fun guess(letter: Char, hint: Boolean) {
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
    private fun wordUnderscored(word: String) {
        val stringBuilder = StringBuilder()
        word.forEach { _ -> stringBuilder.append("_") }
        underscoredLetters = stringBuilder.toString()
    }
    fun gameStart(){
        val key = wordDictionary.keys.random()
        val word = wordDictionary[key]?.random()
        if (word != null){
            wordUnderscored(word)
        }
        answer = word!! //got from ChatGPT, would be a type error otherwise.
        numHints = 0
        currentTries = 0
        playing = true
        hangman = nextHangman()
        hint = key
        numHints = 0
        usedLetters.clear() //used CHAT GPT to clear the used letters
    }
}