package com.example.swith.ui.manage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swith.R
import com.example.swith.databinding.ActivityManageRoundBinding
import com.example.swith.ui.adapter.ManageRoundRVAdapter
import com.example.swith.utils.ToolBarManager
import com.example.swith.viewmodel.RoundViewModel

class ManageRoundActivity : AppCompatActivity() {
    private val viewModel : RoundViewModel by viewModels()
    private lateinit var binding: ActivityManageRoundBinding
    private var groupIdx  = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_round)
        initData()
        initView()
        observeViewModel()
    }

    private fun initData(){
        (intent.hasExtra("gropuId")).let { groupIdx = intent.getIntExtra("groupId", 0) }
        viewModel.groupIdx = groupIdx
    }

    private fun initView(){
        ToolBarManager(this).initToolBar(binding.toolbarManageRound, false, backVisible = true)
        binding.rvManageRound.apply {
            adapter = ManageRoundRVAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observeViewModel(){
        setVisibility(true)
        viewModel.setPastVisible(true)
        viewModel.loadData()

        viewModel.roundLiveData.observe(this, Observer {
            (binding.rvManageRound.adapter as ManageRoundRVAdapter).setData(it.getSessionResList)
        })

        viewModel.mutableScreenState.observe(this, Observer {
            setVisibility(false)
        })
    }

    private fun setVisibility(beforeLoad: Boolean){
        with(binding){
            svManageRound.visibility = if (beforeLoad) View.INVISIBLE else View.VISIBLE
            manageRoundCircularIndicator.visibility = if (beforeLoad) View.VISIBLE else View.INVISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}