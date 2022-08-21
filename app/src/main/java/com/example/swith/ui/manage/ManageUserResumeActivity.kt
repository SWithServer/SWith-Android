package com.example.swith.ui.manage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityManageFinishBinding
import com.example.swith.databinding.ActivityManageUserResumeBinding

class ManageUserResumeActivity:AppCompatActivity(),View.OnClickListener {
    lateinit var binding:ActivityManageUserResumeBinding
    var userIdx : Long? = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_user_resume)
        binding.clickListener = this
        initData()
    }

    fun setResume()
    {

    }

    fun initData()
    {
        (intent.hasExtra("userIdx")).let { userIdx = intent.getLongExtra("userIdx", 0) }
        Log.e("resume","userIdx = ${userIdx}")
        (intent.hasExtra("status").let{
            Log.e("resume status",it.toString())
            if (it)
            {
                binding.btnNo.visibility=View.GONE
                binding.btnYes.visibility=View.GONE
            }
        })
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ib_basic_toolbar_back -> finish()
        }
    }
}