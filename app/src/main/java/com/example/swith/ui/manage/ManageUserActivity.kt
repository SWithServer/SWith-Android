package com.example.swith.ui.manage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityManageUserBinding
import com.example.swith.ui.adapter.ManageUserTabVPAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ManageUserActivity : AppCompatActivity(), View.OnClickListener{
    lateinit var binding : ActivityManageUserBinding
    var groupIdx : Int = -1
    private val tabTitleArray = arrayOf(
        "지원",
        "목록"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_user)
        binding.clickListener = this
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

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ib_basic_toolbar_back -> finish()
        }
    }

}