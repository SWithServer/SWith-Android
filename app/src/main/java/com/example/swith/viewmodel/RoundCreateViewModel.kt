package com.example.swith.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RoundCreateViewModel: ViewModel() {

    fun postRound(){
        viewModelScope.launch {
            // Todo : 회차 정보 post
        }
    }
}