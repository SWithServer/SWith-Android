package com.example.swith.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swith.data.Session
import com.example.swith.repository.create.round.RoundCreateRemoteDataSource
import com.example.swith.repository.create.round.RoundCreateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoundCreateViewModel: ViewModel() {
    private var _sessionIdLiveData = MutableLiveData<Int>()

    val sessionIdLiveData : LiveData<Int>
        get() = _sessionIdLiveData

    private val roundCreateRepository = RoundCreateRepository(RoundCreateRemoteDataSource())

    fun postRound(session: Session){
        viewModelScope.launch {
//            val value = roundCreateRepository.createRound(session)
//            Log.e("RoundCreateViewModel", value.toString())
//            withContext(Dispatchers.Main) {
//                value?.let {
//                    _sessionIdLiveData.value = value!!
//                }
//            }
        }
    }
}