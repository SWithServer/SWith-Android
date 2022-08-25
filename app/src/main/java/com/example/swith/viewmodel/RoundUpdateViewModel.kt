package com.example.swith.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.swith.SwithApplication
import com.example.swith.data.GetSessionRes
import com.example.swith.data.Round
import com.example.swith.data.Session
import com.example.swith.data.SessionModify
import com.example.swith.repository.round.update.RoundUpdateRemoteDataSource
import com.example.swith.repository.round.update.RoundUpdateRepository
import com.example.swith.utils.SingleLiveEvent
import com.example.swith.utils.base.BaseViewModel
import com.example.swith.utils.error.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoundUpdateViewModel: BaseViewModel() {
    private var _sessionLiveEvent = SingleLiveEvent<Any>()

    private var _roundLiveData = SingleLiveEvent<Round>()

    val roundLiveData : LiveData<Round>
        get() = _roundLiveData

    val sessionLiveEvent : LiveData<Any>
        get() = _sessionLiveEvent

    private val roundUpdateRepository = RoundUpdateRepository(RoundUpdateRemoteDataSource())

    // private val userIdx = SharedPrefManager().getLoginData()?.userIdx
    private val userIdx: Long = if (SwithApplication.spfManager.getLoginData() != null) SwithApplication.spfManager.getLoginData()?.userIdx!! else 1


    fun loadPostRound(groupIdx: Long){
        viewModelScope.launch {
            val res = roundUpdateRepository.getPostRound(this@RoundUpdateViewModel, userIdx, groupIdx)
            withContext(Dispatchers.Main){
                if (res == null) mutableScreenState.postValue(ScreenState.RENDER)
                res?.let {
                    mutableScreenState.postValue(ScreenState.RENDER)
                    _roundLiveData.value = res
                }
            }
        }
    }

    fun postRound(session: Session){
        viewModelScope.launch {
            val value = roundUpdateRepository.createRound(this@RoundUpdateViewModel, session)
            withContext(Dispatchers.Main) {
                value?.let {
                    _sessionLiveEvent.call()
                }
            }
        }
    }

    fun deleteRound(sessionIdx: Long){
        viewModelScope.launch {
            val res = roundUpdateRepository.deleteRound(this@RoundUpdateViewModel, sessionIdx)
            withContext(Dispatchers.Main){
                res?.let{
                    mutableScreenState.postValue(ScreenState.LOAD)
                    _sessionLiveEvent.call()
                }
            }
        }
    }

    fun modifyRound(session: SessionModify){
        viewModelScope.launch {
            val res = roundUpdateRepository.modifyRound(this@RoundUpdateViewModel, session)
            withContext(Dispatchers.Main){
                res?.let {
                    _sessionLiveEvent.call()
                }
            }
        }
    }
}