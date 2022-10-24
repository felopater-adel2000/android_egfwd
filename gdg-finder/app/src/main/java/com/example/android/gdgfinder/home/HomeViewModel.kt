package com.example.android.gdgfinder.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class HomeViewModel : ViewModel() {
    private val _navigateToSearch = MutableLiveData<Boolean>()
    val navigateToSearch: LiveData<Boolean>
        get() = _navigateToSearch

    fun onFabClicked()
    {
        Log.i("Felo", "FBA Pressed")
        _navigateToSearch.value = true
    }

    fun onNavigatedToSearch() {
        _navigateToSearch.value = false
    }
}