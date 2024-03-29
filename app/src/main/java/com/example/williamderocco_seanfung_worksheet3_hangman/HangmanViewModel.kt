package com.example.williamderocco_seanfung_worksheet3_hangman

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.Serializable


enum class GameOutcome {
    WIN,
    LOSS,
    PLAYING
}
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
    var showingHints = false
    private val wordDictionary: Map<String, List<String>> = mapOf(
        "country" to listOf("america", "japan", "china", "mexico", "korea"),
        "animal" to listOf("elephant", "zebra", "chicken", "anaconda", "hippo"),
        "fruit" to listOf("apple", "grape", "kiwi", "clementine", "orange"),
        "food" to listOf("pizza", "hamburger", "pasta", "burrito", "ramen"),
        "sports" to listOf("basketball", "baseball", "soccer", "football", "hockey")
    )

    private var buttonMap = mutableMapOf<Char, Button>()
    private val _gameOutcomeLiveData = MutableLiveData<GameOutcome>()
    val gameOutcomeLiveData: LiveData<GameOutcome> = _gameOutcomeLiveData

    // Define MutableLiveData for underscoredLetters and hangmanImage (chatGPT helped with this)
    private val _underscoredLettersLiveData = MutableLiveData<String>()
    val underscoredLettersLiveData: LiveData<String> = _underscoredLettersLiveData

    private val _hangmanImageLiveData = MutableLiveData<Int>()
    val hangmanImageLiveData: LiveData<Int> = _hangmanImageLiveData
    private val _winOrLoseTextLiveData = MutableLiveData<String>()
    val winOrLoseTextLiveData: LiveData<String> = _winOrLoseTextLiveData

    private val _hintLiveData = MutableLiveData<String>()
    val hintLiveData: LiveData<String> = _hintLiveData


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
        showingHints = false
        hangman = nextHangman()
        _hintLiveData.value = ""
        _gameOutcomeLiveData.value = GameOutcome.PLAYING
        _winOrLoseTextLiveData.value = ""
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
        keyboardRow4: LinearLayout,
        isNewGame: Boolean
    ) {
        // Clear existing buttons from each LinearLayout if it's a new game
        if (isNewGame) {
            keyboardRow1.removeAllViews()
            keyboardRow2.removeAllViews()
            keyboardRow3.removeAllViews()
            keyboardRow4.removeAllViews()
        }

        val alphabet = "abcdefghijklmnopqrstuvwxyz"

        // Loop to create buttons for each letter 'a' to 'z'
        for ((index, char) in alphabet.withIndex()) {
            val button = Button(context)
            button.text = char.toString()
            button.layoutParams = LinearLayout.LayoutParams(
                100,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            // Define button ID dynamically
            val buttonId = View.generateViewId() // Generate unique ID for each button
            button.id = buttonId


            button.setOnClickListener {
                if (playing) {
                    this.guess(context, char)
                    button.setBackgroundColor(Color.parseColor("#888888"))
                }
            }

            // Add button reference to the map
            buttonMap[char] = button

            // Determine which row to add the button to
            when {
                index < 8 -> keyboardRow1.addView(button)
                index < 16 -> keyboardRow2.addView(button)
                index < 23 -> keyboardRow3.addView(button)
                else -> keyboardRow4.addView(button)
            }
        }
    }

    private fun guess(context: Context, letter: Char) {
        Log.d("button map: ", buttonMap.toString())
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
                if(!showingHints) currentTries++
                hangman = nextHangman()
            }

            // case 2: hit max tries
            if (currentTries == maxTries){
                playing = false
                win = false
                _gameOutcomeLiveData.value = GameOutcome.LOSS
            }

            // case 3: guessed correct word
            underscoredLetters = underscoredWord
            _underscoredLettersLiveData.value = underscoredLetters // Update underscoredLettersLiveData
            if(underscoredLetters.lowercase() == answer){
                playing = false
                win = true
                _gameOutcomeLiveData.value = GameOutcome.WIN
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



    fun obtainHint(context: Context): Int {
        Log.d("current hints:", numHints.toString())
        Log.d("used letters:", usedLetters.toString())
        var returnVal = -1

        // case 2: reached maximum number of tries
        if (currentTries == maxTries - 1) {
            playing = false
            win = false
            return -1
        }

        when (numHints) {
            // first hint: display message for hint suggestion
            0 -> {
                _hintLiveData.value = hint
                numHints++
                return 0
            }
            // second hint: hide half of letters that are not in word
            1 -> {
                showingHints = true
                returnVal = 1
                hideLetters(context)
                showingHints = false
            }
            // third hint: show all vowels in word (and disable remaining vowels)
            2 -> {
                showingHints = true
                returnVal = 2
                displayVowels()
                showingHints = false
            }
            // No more hints available
            else -> {
                Toast.makeText(context, "No more hints :(", Toast.LENGTH_SHORT).show()
                return -1
            }
        }

        currentTries++
        numHints++
        hangman = nextHangman()

        return returnVal
    }

    private fun hideLetters(context: Context) {
        val numToRemove = (26 - usedLetters.size) / 2
        var numRemoved = 0
        for (letter in 'a'..'z') {
            val l = letter.toString().lowercase()
            // check that hint is in NOT in answer or usedLetters
            if (l !in answer.lowercase() && !usedLetters.contains(l)) {
                // make guess and increment count for removed letters
                // Find the button corresponding to the letter from the buttonMap
                val button = buttonMap[letter]
                button?.performClick() // Trigger the click event of the button if it exists
                numRemoved++
            }
            if (numRemoved == numToRemove){
                return
            }
        }
    }

    private fun displayVowels(){
        val vowels = "aeiou"

        for (vowel in vowels) {
            if(vowel.toString() !in usedLetters){
                // make guess and increment count for removed letters
                // Find the button corresponding to the letter from the buttonMap
                val button = buttonMap[vowel]
                button?.performClick() // Trigger the click event of the button if it exists
            }
        }
    }

    companion object {//Chat GPT wrote all the code below so that I can save all this time
        private const val KEY_UNDERSCORED_LETTERS = "underscored_letters"
        private const val KEY_HANGMAN_IMAGE = "hangman_image"
        private const val KEY_ANSWER = "answer"
        private const val KEY_HINT = "hint"
        private const val KEY_USED_LETTERS = "used_letters"
        private const val KEY_NUM_HINTS = "num_hints"
        private const val KEY_CURRENT_TRIES = "current_tries"
        private const val KEY_PLAYING = "playing"
        private const val KEY_WIN = "win"
        private const val KEY_SHOWING_HINTS = "key_showing_hints"
        private const val KEY_BUTTON_MAP = "key_button_map"
    }
    
    // Save instance state (overrides normal savInstanceState and restoreInstanceState functions)
    fun saveInstanceState(outState: Bundle) {
        outState.putString(KEY_UNDERSCORED_LETTERS, underscoredLetters)
        outState.putInt(KEY_HANGMAN_IMAGE, hangman)
        outState.putString(KEY_ANSWER, answer)
        outState.putString(KEY_HINT, hint)
        outState.putStringArrayList(KEY_USED_LETTERS, ArrayList(usedLetters))
        outState.putInt(KEY_NUM_HINTS, numHints)
        outState.putInt(KEY_CURRENT_TRIES, currentTries)
        outState.putBoolean(KEY_PLAYING, playing)
        outState.putBoolean(KEY_WIN, win)
        outState.putBoolean(KEY_SHOWING_HINTS, showingHints)
        outState.putSerializable(KEY_BUTTON_MAP, buttonMap as Serializable)
    }
    fun restoreInstanceState(savedInstanceState: Bundle) {
        underscoredLetters = savedInstanceState.getString(KEY_UNDERSCORED_LETTERS, "")
        _underscoredLettersLiveData.value = underscoredLetters
        hangman = savedInstanceState.getInt(KEY_HANGMAN_IMAGE)
        _hangmanImageLiveData.value = hangman
        answer = savedInstanceState.getString(KEY_ANSWER, "")
        hint = savedInstanceState.getString(KEY_HINT, "")
        usedLetters.addAll(savedInstanceState.getStringArrayList(KEY_USED_LETTERS) ?: emptyList())
        numHints = savedInstanceState.getInt(KEY_NUM_HINTS)
        currentTries = savedInstanceState.getInt(KEY_CURRENT_TRIES)
        playing = savedInstanceState.getBoolean(KEY_PLAYING)
        win = savedInstanceState.getBoolean(KEY_WIN)
        showingHints = savedInstanceState.getBoolean(KEY_SHOWING_HINTS)
        buttonMap = savedInstanceState.getSerializable(KEY_BUTTON_MAP) as? MutableMap<Char, Button> ?: mutableMapOf()
    }
}