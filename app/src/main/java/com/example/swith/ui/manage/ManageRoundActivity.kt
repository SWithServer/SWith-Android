package com.example.swith.ui.manage

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.GetSessionRes
import com.example.swith.databinding.ActivityManageRoundBinding
import com.example.swith.ui.adapter.ManageRoundRVAdapter
import com.example.swith.ui.dialog.CustomConfirmDialog
import com.example.swith.utils.ItemTouchHelperListener
import com.example.swith.utils.SwipeController
import com.example.swith.utils.ToolBarManager
import com.example.swith.viewmodel.RoundViewModel
import java.lang.Float.max
import java.lang.Float.min


class ManageRoundActivity : AppCompatActivity(){
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
            adapter = ManageRoundRVAdapter().apply {
                setCustomListener(object: ManageRoundRVAdapter.CustomListener{
                    override fun onClick(round: GetSessionRes) {
                        startActivity(Intent(applicationContext, ManageRoundModifyActivity::class.java).apply {
                            putExtra("curRound", round)
                        })
                    }

                    override fun onDeleteClick(round: GetSessionRes) {
                        CustomConfirmDialog("회차 삭제", "${round.sessionNum}회차를 삭제하시겠습니까?").apply {
                            setCustomListener(object: CustomConfirmDialog.CustomListener{
                                override fun onConfirm() {
                                    Log.e("delete complete", "${round.sessionNum}회차 삭제 완료")
                                }
                            })
                        }.show(supportFragmentManager, "roundDelete")
                    }
                })
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val itemTouchCallback = ItemTouchHelper(SwipeController(this.adapter as ManageRoundRVAdapter))
            itemTouchCallback.attachToRecyclerView(this)
        }
    }

    private fun observeViewModel(){
        setVisibility(true)
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