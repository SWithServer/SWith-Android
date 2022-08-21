package com.example.swith.ui.manage

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityManageUserProfileBinding

class ManageUserProfileActivity : AppCompatActivity(){
    var userIdx : Long? = -1
    lateinit var binding : ActivityManageUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_user_profile)
        initData()
    }

    fun initData()
    {
        (intent.hasExtra("userIdx")).let { userIdx = intent.getLongExtra("userIdx", 0) }
        Log.e("summer","userIdx = ${userIdx}")
    }
}