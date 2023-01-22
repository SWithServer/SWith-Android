package com.example.swith.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.data.R
import com.example.data.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityLoginBinding

    //뒤로가기 두번 누를때 꺼지게
    private var mBackBtnPresses: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener = this@LoginActivity
    }

    private fun initData() {
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_login -> {
                Intent(this@LoginActivity, SnsLoginActivity::class.java).run {
                    startActivity(this)
                }
            }
        }
    }

    override fun onBackPressed() {
        binding.run {
            Log.e("doori", "onbackPressed ${mBackBtnPresses.toString()}")
            if (mBackBtnPresses) {
                mBackBtnPresses = false
                finishApp()
            } else {
                Snackbar.make(root, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Snackbar.LENGTH_LONG).show()
                mBackBtnPresses = true
                Handler(Looper.getMainLooper()).postDelayed({ mBackBtnPresses = false }, 2500)
            }
        }
    }

    //앱 완전종료
    private fun finishApp() {
        ActivityCompat.finishAffinity(this)
        System.runFinalization()
        exitProcess(0)
    }
}