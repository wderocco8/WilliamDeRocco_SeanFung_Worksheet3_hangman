package com.example.williamderocco_seanfung_worksheet3_hangman

import android.content.Context
import android.nfc.Tag
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "hangmanView"
class HangmanViewModel : ViewModel() {
    // class variables
    var answer = ""
    var hint = ""
    var usedLetters = mutableListOf<String>()
    private var numHints = 0
    private var maxTries = 6
    var currentTries = 0
    lateinit var underscoredLetters: String
    private var hangman: Int = R.drawable.state0
    var playing = false
    var win = false
    var firstHintShowed = false
    private val wordDictionary: Map<String, List<String>> = mapOf(
        "country" to listOf("america", "japan", "china", "mexico", "korea"),
        "animal" to listOf("elephant", "zebra", "chicken", "anaconda", "hippo"),
        "fruit" to listOf("apple", "grape", "kiwi", "clementine", "orange"),
        "food" to listOf("pizza", "hamburger", "pasta", "burrito", "ramen"),
        "sports" to listOf("basketball", "baseball", "soccer", "football", "hockey")
    )

    // Define MutableLiveData for underscoredLetters and hangmanImage (chatGPT helped with this)
    private val _underscoredLettersLiveData = MutableLiveData<String>()
    val underscoredLettersLiveData: LiveData<String> = _underscoredLettersLiveData

    private val _hangmanImageLiveData = MutableLiveData<Int>()
    val hangmanImageLiveData: LiveData<Int> = _hangmanImageLiveData


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
        // create a string of underscores of length : word.length
        word.forEach { _ -> sb.append("_")}
        underscoredLetters = sb.toString()
        _underscoredLettersLiveData.value = underscoredLetters // Update underscoredLettersLiveData
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
        if (!playing) {
            Toast.makeText(context, "Select 'new game' to start!", Toast.LENGTH_SHORT).show()
        } else if (usedLetters.contains(letter.toString())) {
            Toast.makeText(context, "Letter already selected", Toast.LENGTH_SHORT).show()
        } else {
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
            _underscoredLettersLiveData.value = underscoredLetters // Update underscoredLettersLiveData
            if(underscoredLetters.lowercase() == answer){
                playing = false
                win = true
            }
        }

    }

    fun nextHangman(): Int {
        val hangmanImage = when (currentTries) {
            0 -> R.drawable.state0
            1 -> R.drawable.state1
            2 -> R.drawable.state2
            3 -> R.drawable.state3
            4 -> R.drawable.state4
            5 -> R.drawable.state5
            else -> R.drawable.state6
        }
        _hangmanImageLiveData.value = hangmanImage // Update hangmanImageLiveData
        return hangmanImage
    }

    fun hint(): Int {
        var returnVal: Int
        // case 1: No more hints available
        if (numHints >= 2) {
            return -1
        }

        // case 2: reached maximum number of tries
        if (currentTries == maxTries - 1) {
            playing = false
            win = false
        }

        when (numHints) {
            0 -> {
                returnVal = 1
//            hideLetters()
            }
            1 -> {
                returnVal = 2
//            showVowels()
            }
            else -> {
                return -1 // No more hints available
            }
        }

        currentTries++
        numHints++
        hangman = nextHangman()

        return returnVal
    }

}