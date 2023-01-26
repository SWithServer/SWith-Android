package com.example.data.utils

enum class ErrorType {
    NETWORK,    // IO
    TIMEOUT,    // Socket
    UNKNOWN,    // Something Else
    SESSION_EXPIRED // Session 만료
}