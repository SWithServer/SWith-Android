package com.example.swith.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.swith.data.Session
import com.example.swith.repository.create.round.RoundCreateRemoteDataSource
import com.example.swith.repository.create.round.RoundCreateRepository
import com.example.swith.utils.SingleLiveEvent
import com.example.swith.utils.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoundCreateViewModel: BaseViewModel() {
    private var _sessionLiveEvent = SingleLiveEvent<Any>()

    val sessionLiveEvent : LiveData<Any>
        get() = _sessionLiveEvent

    private val roundCreateRepository = RoundCreateRepository(RoundCreateRemoteDataSource())

    fun postRound(session: Session){
        viewModelScope.launch {
            val value = roundCreateRepository.createRound(this@RoundCreateViewModel, session)
            Log.e("RoundCreateViewModel", value.toString())
            withContext(Dispatchers.Main) {
                value?.let {
                    _sessionLiveEvent.call()
                }
            }
        }
    }
}