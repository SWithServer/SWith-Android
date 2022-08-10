package com.example.swith.utils.base

import com.example.swith.utils.error.ErrorType
import com.example.swith.utils.error.RemoteErrorEmitter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException

abstract class BaseRepository {
    companion object {
        private const val MESSAGE_KEY = "message"
        private const val ERROR_KEY = "error"
    }

    suspend inline fun <T> safeApiCall(emitter: RemoteErrorEmitter, crossinline callFunction: suspend () -> T): T? {
        return try{
            val result  = withContext(Dispatchers.IO){ callFunction.invoke() }
            result
        }catch (e: Exception){
            withContext(Dispatchers.Main){
                e.printStackTrace()
                when(e){
                    is HttpException -> {
                        if (e.code() == 401) emitter.onError(ErrorType.SESSION_EXPIRED)
                        else{
                            val body = e.response()?.errorBody()
                            emitter.onError(getErrorMessage(body))
                        }
                    }
                    is SocketTimeoutException -> emitter.onError(ErrorType.TIMEOUT)
                    is IOException -> emitter.onError(ErrorType.NETWORK)
                    else -> emitter.onError(ErrorType.UNKNOWN)
                }
            }
            null
        }
    }

    fun getErrorMessage(responseBody: ResponseBody?): String {
        return try {
            val jsonObject = JSONObject(responseBody!!.string())
            when {
                jsonObject.has(MESSAGE_KEY) -> jsonObject.getString(MESSAGE_KEY)
                jsonObject.has(ERROR_KEY) -> jsonObject.getString(ERROR_KEY)
                else -> "Something wrong happened"
            }
        } catch (e: Exception) {
            "Something wrong happened"
        }
    }

}
