package com.example.swith.utils.base

import androidx.lifecycle.ViewModel
import com.example.swith.utils.SingleLiveEvent
import com.example.swith.utils.error.ErrorType
import com.example.swith.utils.error.RemoteErrorEmitter
import com.example.swith.utils.error.ScreenState

abstract class BaseViewModel : ViewModel(), RemoteErrorEmitter {
    val mutableScreenState = SingleLiveEvent<ScreenState>()
    val mutableErrorMessage = SingleLiveEvent<String>()
    val mutableErrorType = SingleLiveEvent<ErrorType>()

    override fun onError(msg: String) {
        mutableErrorMessage.postValue(msg)
    }

    override fun onError(errorType: ErrorType) {
        mutableErrorType.postValue(errorType)
    }
}