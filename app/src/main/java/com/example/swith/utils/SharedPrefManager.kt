package com.example.swith.utils
import android.content.Context
import android.content.SharedPreferences
import com.example.swith.data.LoginData
import com.google.gson.Gson

class SharedPrefManager (private val context: Context) {

    companion object {
        private const val FILENAME = ""
        private const val LOGIN_DATA: String = "login_data"
        private const val FCM_NAME="fcm"
        private const val FCM_DATA: String="fcm_data"
    }

    fun clearAll() {
        val prefs: SharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    /**
     * 로그인 데이터 저장
     */
    fun setLoginData(userIdx: Long, jwt: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)

        Gson().apply {
            val loginData: LoginData = LoginData(userIdx,jwt)
            val jsonToString: String = toJson(loginData)
            prefs.edit().putString(LOGIN_DATA, jsonToString).apply()
        }
    }
    /**
     * 로그인 데이터 반환
     * @return LoginData?
     */
    fun getLoginData(): LoginData? {
        val prefs: SharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        Gson().apply {
            val jsonToString: String = prefs.getString(LOGIN_DATA, "")?:""
            return fromJson(jsonToString, LoginData::class.java)
        }
    }

    /**
     * 로그인 데이터 삭제
     */
    fun deleteLoginData() {
        val prefs: SharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        prefs.edit().remove(LOGIN_DATA).apply()
    }

    /**
     * fcm토큰저장
     */
    fun setFcmToken(token:String){
        val prefs:SharedPreferences=context.getSharedPreferences(FCM_NAME,Context.MODE_PRIVATE)
        prefs.edit().putString(FCM_DATA,token).apply()
    }

    fun getFcmToken():String?{
        val prefs:SharedPreferences=context.getSharedPreferences(FCM_NAME,Context.MODE_PRIVATE)
        return prefs.getString(FCM_DATA,"")
    }
}