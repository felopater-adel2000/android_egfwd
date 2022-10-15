/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.launch


class SleepTrackerViewModel(val database: SleepDatabaseDao, application: Application) : AndroidViewModel(application)
{
    private var tonight = MutableLiveData<SleepNight?>()

    private val nights = database.getAllNights()

    private val _navigationToSleepQuality = MutableLiveData<SleepNight>()
    val navigationToSleepQuality: LiveData<SleepNight>
        get() = _navigationToSleepQuality

    val nightsString = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources)
    }
    val startButtonVisible = Transformations.map(tonight) {
        null == it
    }
    val stopButtonVisible = Transformations.map(tonight) {
        null != it
    }
    val clearButtonVisible = Transformations.map(nights) {
        it?.isNotEmpty()
    }

    private var _showSnackBarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent

    fun doShowSnackBar()
    {
        _showSnackBarEvent.value = false
    }

    init {
        initializeTonight()
    }
    private fun initializeTonight() {
        viewModelScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase():  SleepNight?
    {
        var night = database.getTonight()

        if (night?.endTimeMilli != night?.startTimeMilli) {
            night = null
        }
        return night
    }

    fun onStartTracking()
    {
        viewModelScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(night: SleepNight)
    {database.insert(night)}



    fun onStopTracking() {
        Log.i("Felo", "onStop Pressed")
        viewModelScope.launch {
            val oldNight = tonight.value ?: return@launch
            Log.i("Felo", "oldNight.Start = ${oldNight.startTimeMilli}")
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _navigationToSleepQuality.value = oldNight
        }
    }

    fun onStopTracking2() {
        Log.i("Felo", "onStop Pressed")
        viewModelScope.launch {
            val oldNight = tonight.value ?: return@launch
            Log.i("Felo", "oldNight.Start = ${oldNight.startTimeMilli}")
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _navigationToSleepQuality.value = oldNight
        }
    }

    private suspend fun update(night: SleepNight) {
        database.update(night)
    }

    fun onClear() {
        viewModelScope.launch {
            clear()
            tonight.value = null
        }
        _showSnackBarEvent.value = true
    }

    suspend fun clear() {
        database.clear()
    }

    override fun onCleared() {
        super.onCleared()
        _showSnackBarEvent.value = true
    }

}

