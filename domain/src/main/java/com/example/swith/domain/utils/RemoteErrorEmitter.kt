package com.example.swith.domain.utils

import com.example.data.utils.ErrorType

interface RemoteErrorEmitter {
    fun onError(msg: String)
    fun onError(errorType: ErrorType)
}