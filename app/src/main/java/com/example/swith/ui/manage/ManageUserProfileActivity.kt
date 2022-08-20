package com.example.swith.ui.manage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityManageUserProfileBinding

class ManageUserProfileActivity : AppCompatActivity(){
    lateinit var binding : ActivityManageUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_user_profile)

    }
}