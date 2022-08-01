package com.example.swith.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepository {
    suspend inline fun <T> safeApiCall(crossinline callFunction: suspend () -> T): T? {
        return try{
            val result  = withContext(Dispatchers.IO){ callFunction.invoke() }
            result
        }catch (e: Exception){
            // Todo : 오류 별 로직 추가 할지 말지
            null
        }
    }

}
