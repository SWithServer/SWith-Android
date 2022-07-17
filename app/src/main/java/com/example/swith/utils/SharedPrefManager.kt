package com.example.swith.utils
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class SharedPrefManager (private val context: Context) {

    companion object {
        private const val FILENAME = ""
        private const val LOGIN: String = "login_data"
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
    fun setLoginData(userIdx: Int, jwt: String,certified: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)

        Gson().apply {
            //val loginData: LoginData = LoginData(userIdx,jwt,certified)
            //val jsonToString: String = toJson(loginData)
            //prefs.edit().putString(PREF_LOGIN_DATA, jsonToString).apply()
        }
    }
    /**
     * 로그인 데이터 반환
     * @return LoginData?
     */
    /*fun getLoginData(): LoginData? {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE)
        Gson().apply {
            val jsonToString: String = prefs.getString(PREF_LOGIN_DATA, "")?:""
            return fromJson(jsonToString, LoginData::class.java)
        }
    }*/

    /**
     * 로그인 데이터 삭제
     */
    fun deleteLoginData() {
        val prefs: SharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        prefs.edit().remove(LOGIN).apply()
    }
}