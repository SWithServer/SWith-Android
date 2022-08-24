package com.example.swith.ui.manage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityManageBinding

class ManageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityManageBinding
    private val groupIdx: Int by lazy{
        if(intent.hasExtra("groupId")) intent.getIntExtra("groupId", 0)
        else 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage)
        Log.e("groupIdx", groupIdx.toString())
        initListener()
    }

    private fun initListener(){
        binding.clickListener = this
        with(binding){
            layoutManageStudy.setOnClickListener { startActivity(Intent(this@ManageActivity,ManageStudyModifyActivity::class.java).apply{
                putExtra("groupIdx",groupIdx)
            }) }
            layoutManageRound.setOnClickListener {
                startActivity(Intent(this@ManageActivity, ManageRoundActivity::class.java).apply {
                    putExtra("groupId", groupIdx)
            }) }
            layoutManageAttend.setOnClickListener { startActivity(Intent(this@ManageActivity, ManageAttendActivity::class.java).apply {
                putExtra("groupId", groupIdx)
            }) }
            layoutManageUser.setOnClickListener { startActivity(Intent(this@ManageActivity,ManageUserActivity::class.java).apply{
                putExtra("groupIdx",groupIdx)
            }) }
            layoutManageFinish.setOnClickListener { startActivity(Intent(this@ManageActivity,ManageFinishActivity::class.java).apply{
                putExtra("groupIdx",groupIdx)
            })}
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ib_manage_toolbar_back -> {
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        finish()
    }
}