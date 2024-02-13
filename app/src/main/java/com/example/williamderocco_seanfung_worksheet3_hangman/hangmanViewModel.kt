package com.example.williamderocco_seanfung_worksheet3_hangman

import androidx.lifecycle.ViewModel

private const val TAG = "hangmanView"
class hangmanViewModel : ViewModel() {
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
            val stringBuild = StringBuilder(underscoredWord).also { it.setCharAt(index, letter)}
            underscoredWord = stringBuild.toString()
        }

        if (indexLetter.isEmpty()){
            if(!hint)currentTries++
            hangman = nextHangman()
        }

        if (currentTries == maxTries){
            playing = false
            win = false
        }

        underscoredLetters = underscoredWord
        if(underscoredLetters.lowercase() == answer){
            playing = false
            win = true
        }
    }

    fun nextHangman(): Int{
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
        answer = word!!
        numHints = 0
        currentTries = 0
        playing = true
        hangman = nextHangman()
        hint = key
        numHints = 0
        usedLetters.clear() //used CHAT GPT to clear the used letters
    }
}