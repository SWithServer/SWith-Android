package com.example.swith.ui.manage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityManageFinishBinding


class ManageFinishActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityManageFinishBinding
    var groupIdx : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_manage_finish)

        initData()
        with(binding)
        {
            btnYes.setOnClickListener{
                finishStudy(groupIdx)
            }
            btnNo.setOnClickListener{
                finish()
            }
            clickListener = this@ManageFinishActivity
        }
    }

    fun initData()
    {
        (intent.hasExtra("groupIdx")).let { groupIdx = intent.getIntExtra("groupIdx", 0) }
        Log.e("summer","groupIdx = ${groupIdx}")
    }

    fun finishStudy(groupIdx : Int){
        // del retrofit 부분
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ib_basic_toolbar_back -> finish()
        }
    }
}