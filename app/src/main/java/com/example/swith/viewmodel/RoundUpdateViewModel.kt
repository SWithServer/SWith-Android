package com.example.swith.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.swith.data.Session
import com.example.swith.repository.round.update.RoundUpdateRemoteDataSource
import com.example.swith.repository.round.update.RoundUpdateRepository
import com.example.swith.utils.SingleLiveEvent
import com.example.swith.utils.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoundUpdateViewModel: BaseViewModel() {
    private var _sessionLiveEvent = SingleLiveEvent<Any>()

    val sessionLiveEvent : LiveData<Any>
        get() = _sessionLiveEvent

    private val roundCreateRepository = RoundUpdateRepository(RoundUpdateRemoteDataSource())

    fun postRound(session: Session){
        viewModelScope.launch {
            val value = roundCreateRepository.createRound(this@RoundUpdateViewModel, session)
            Log.e("RoundCreateViewModel", value.toString())
            withContext(Dispatchers.Main) {
                value?.let {
                    _sessionLiveEvent.call()
                }
            }
        }
    }
}