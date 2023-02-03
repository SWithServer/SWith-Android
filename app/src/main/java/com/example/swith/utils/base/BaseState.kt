package com.example.swith.utils.base

sealed class BaseState<out T: Any> {
    // 아무 상태 아님
    object None : BaseState<Nothing>()
    // 로딩 상태
    object Loading : BaseState<Nothing>()
    // 성공
    data class Success<out T : Any>(val value: T) : BaseState<T>()
    // 에러
    data class Error<out T : Any>(val message: String?) : BaseState<T>()
}