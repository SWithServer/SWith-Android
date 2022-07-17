package com.example.swith.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    lateinit var binding:  ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash)
        initData()
        initView()
    }

    private fun initView() {
        //TODO("Not yet implemented")
    }

    private fun initData() {
        //TODO 로그인여부에 따라서 메인화면 or 로그인화면으로 넘어간다.
    }
}