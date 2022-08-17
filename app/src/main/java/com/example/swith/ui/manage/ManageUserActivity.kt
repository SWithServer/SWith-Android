package com.example.swith.ui.manage

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityManageUserBinding
import com.example.swith.ui.adapter.ManageUserTabVPAdapter
import com.example.swith.utils.ToolBarManager
import com.google.android.material.tabs.TabLayoutMediator

class ManageUserActivity : AppCompatActivity(){
    lateinit var binding : ActivityManageUserBinding
    var groupIdx : Int = -1
    private val tabTitleArray = arrayOf(
        "지원",
        "목록"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_user)
        ToolBarManager(this).initToolBar(binding.toolbarManageUser, false, backVisible = true)
        initData()
        viewPager()
    }

    fun viewPager(){
        val viewPager = binding.applicationViewPager
        val tabLayout = binding.applicationTabLayout

        viewPager.adapter = ManageUserTabVPAdapter(supportFragmentManager,lifecycle)
        TabLayoutMediator(tabLayout,viewPager){ tab, position-> tab.text = tabTitleArray[position]
        }.attach()
    }

    fun initData()
    {
        (intent.hasExtra("groupIdx")).let { groupIdx = intent.getIntExtra("groupIdx", 0) }
        Log.e("summer","groupIdx = ${groupIdx}")
    }

}