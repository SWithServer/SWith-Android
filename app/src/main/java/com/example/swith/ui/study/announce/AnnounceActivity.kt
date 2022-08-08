package com.example.swith.ui.study.announce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swith.R
import com.example.swith.databinding.ActivityAnnounceBinding
import com.example.swith.ui.adapter.AnnounceRVAdapter
import com.example.swith.utils.ToolBarManager
import com.example.swith.viewmodel.AnnounceViewModel

class AnnounceActivity : AppCompatActivity(){
    private val viewModel : AnnounceViewModel by viewModels()
    // 매니저
    private val isManager: Boolean by lazy {
        if (intent.hasExtra("manager"))
            intent.getBooleanExtra("manager", false)
        else false
    }
    // group Idx
    private val groupIdx: Int by lazy {
        if (intent.hasExtra("groupIdx"))
            intent.getIntExtra("groupIdx", 0)
        else 0
    }

    lateinit var binding: ActivityAnnounceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_announce)

        ToolBarManager(this).initToolBar(binding.tbAnnounce,
            titleVisible = false,
            backVisible = true
        )

        initView()
        observeViewModel()
    }

    private fun initView() {
        with(binding){
            rvAnnounce.adapter = AnnounceRVAdapter(isManager).apply {
                // 리스너 추가
            }
            rvAnnounce.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        }
    }


    private fun observeViewModel() {
        setVisibility(true)
        viewModel.loadData(groupIdx)

        viewModel.announceLiveData.observe(this, Observer {
            (binding.rvAnnounce.adapter as AnnounceRVAdapter).setData(it.announces)
        })

        viewModel.mutableScreenState.observe(this, Observer{
            setVisibility(false)
        })
    }

    private fun setVisibility(beforeLoad: Boolean){
        with(binding){
            svAnnounce.visibility = if(beforeLoad) View.INVISIBLE else View.VISIBLE
            announceCircularIndicator.visibility = if (beforeLoad) View.VISIBLE else View.INVISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}