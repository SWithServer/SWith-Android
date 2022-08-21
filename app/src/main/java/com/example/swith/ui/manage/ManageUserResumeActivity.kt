package com.example.swith.ui.manage

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityManageFinishBinding
import com.example.swith.databinding.ActivityManageUserResumeBinding

class ManageUserResumeActivity:AppCompatActivity() {
    lateinit var binding:ActivityManageUserResumeBinding
    var userIdx : Long? = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_user_resume)

        initData()
    }

    fun initData()
    {
        (intent.hasExtra("userIdx")).let { userIdx = intent.getLongExtra("userIdx", 0) }
        Log.e("resume","userIdx = ${userIdx}")
    }
}