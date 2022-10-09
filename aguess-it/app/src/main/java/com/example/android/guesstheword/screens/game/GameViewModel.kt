package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

// is a ViewModel class for GameFragment
class GameViewModel : ViewModel()
{
    // The current word
    var word = MutableLiveData<String>()

    // The current score
    private var _score = MutableLiveData<Int>()

    val score: LiveData<Int>
        get() = _score

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish
    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String>
        get() = _timerText

    val currentTimeString = Transformations.map(timerText){ time ->
        time
    }

    //timer
    val timer: CountDownTimer

    init
    {
        Log.i("Felo", "GameViewModel created")
        _eventGameFinish.value = false
        resetList()
        nextWord()
        _score.value = 0
        timer = object: CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onFinish()
            {
                _eventGameFinish.value = true
            }

            override fun onTick(p0: Long)
            {
                _timerText.value = (p0/1000).toString()
            }
        }

        timer.start()
    }
    fun onGameFinishCompleted()
    {
        _eventGameFinish.value = false
    }

    // this function will called when Activity or Fragment destroyed
    override fun onCleared()
    {
        super.onCleared()
    }
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord()
    {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
//            //gameFinished()
//            _eventGameFinish.value = true
            resetList()
        }
        word.value = wordList.removeAt(0)

    }

    /** Methods for buttons presses **/

    fun onSkip()
    {
        _score.value = _score.value!! - 1
        nextWord()
    }

    fun onCorrect()
    {
        _score.value = _score.value!! + 1
        nextWord()
    }

    companion object
    {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 10000L
    }
}