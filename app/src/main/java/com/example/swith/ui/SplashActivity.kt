package com.example.swith.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivitySplashBinding
import com.example.swith.ui.login.LoginActivity
import com.example.swith.utils.SharedPrefManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        initData()
        initView()
    }

    private fun initView() {
        //SharedPref에 loginData존재 유무에 따라 로그인 or 메인화면으로 넘어간다.
        val loginData = SharedPrefManager(this@SplashActivity).getLoginData()
        Log.e("doori", loginData.toString())
        if (loginData != null) {
            goMainpage()
        } else {
            goLoginpage()
        }
    }

    private fun initData() {
    }

    private fun goLoginpage() {
        //1초동안 로고보이기
        CoroutineScope(Dispatchers.Default).launch {
            delay(1000)
            Intent(this@SplashActivity, LoginActivity::class.java).run {
                startActivity(this)
                finishAffinity()
            }
        }
    }

    private fun goMainpage() {
        //1초동안 로고보이기
        CoroutineScope(Dispatchers.Default).launch {
            delay(1000)
            Intent(this@SplashActivity, MainActivity::class.java).run {
                startActivity(this)
                finishAffinity()
            }
        }
    }
}