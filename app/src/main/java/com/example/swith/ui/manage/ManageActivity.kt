package com.example.swith.ui.manage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityManageBinding
import com.example.swith.utils.ToolBarManager

class ManageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageBinding
    private val groupIdx: Int by lazy{
        if(intent.hasExtra("groupId")) intent.getIntExtra("groupId", 0)
        else 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage)
        ToolBarManager(this).initToolBar(binding.toolbarManage,
            titleVisible = false,
            backVisible = true
        )
        Log.e("groupIdx", groupIdx.toString())
        initListener()
    }

    private fun initListener(){
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}