package com.example.swith.ui.manage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.entity.GetSessionRes
import com.example.swith.R
import com.example.swith.databinding.ActivityManageRoundBinding
import com.example.swith.ui.adapter.ManageRoundRVAdapter
import com.example.swith.ui.dialog.CustomAlertDialog
import com.example.swith.ui.dialog.CustomConfirmDialog
import com.example.swith.utils.SwipeController
import com.example.swith.utils.error.ScreenState
import com.example.swith.viewmodel.RoundUpdateViewModel

class ManageRoundActivity : AppCompatActivity(), View.OnClickListener {
    private val viewModel: RoundUpdateViewModel by viewModels()
    private lateinit var binding: ActivityManageRoundBinding
    private val groupIdx by lazy {
        intent.getLongExtra("groupId", 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_round)
        initView()
        observeViewModel()
    }

    private fun initView() {
        binding.clickListener = this
        binding.rvManageRound.apply {
            adapter = ManageRoundRVAdapter().apply {
                setCustomListener(object : ManageRoundRVAdapter.CustomListener {
                    override fun onClick(round: GetSessionRes) {
                        startActivity(
                            Intent(
                                applicationContext,
                                ManageRoundModifyActivity::class.java
                            ).apply {
                                putExtra("curRound", round)
                                putExtra(
                                    "minuteMin",
                                    viewModel.roundLiveData.value?.attendanceValidTime
                                )
                            })
                    }

                    override fun onDeleteClick(round: GetSessionRes) {
                        CustomConfirmDialog("회차 삭제", "${round.sessionNum}회차를 삭제하시겠습니까?").apply {
                            setCustomListener(object : CustomConfirmDialog.CustomListener {
                                override fun onConfirm() {
                                    dismiss()
                                    viewModel.deleteRound(round.sessionIdx)
                                }
                            })
                        }.show(supportFragmentManager, "roundDelete")
                    }
                })
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val itemTouchHelper =
                ItemTouchHelper(SwipeController(this.adapter as ManageRoundRVAdapter))
            itemTouchHelper.attachToRecyclerView(this)
        }
    }

    private fun observeViewModel() {
        reloadData()

        viewModel.roundLiveData.observe(this, Observer {
            (binding.rvManageRound.adapter as ManageRoundRVAdapter).setData(it.getSessionResList)
        })

        // 스크린 상태 변경
        viewModel.mutableScreenState.observe(this, Observer {
            if (it == ScreenState.RENDER) setVisibility(
                false,
                viewModel.roundLiveData.value?.getSessionResList.isNullOrEmpty()
            )
        })

        viewModel.mutableErrorMessage.observe(this, Observer {
            CustomAlertDialog("회차 삭제 실패", it.toString()).show(supportFragmentManager, "회차 삭제 실패")
        })

        viewModel.sessionLiveEvent.observe(this, Observer {
            CustomAlertDialog("회차 삭제 완료", "해당 회차가 삭제되었습니다.").show(
                supportFragmentManager,
                "회차 삭제 완료"
            )
            reloadData()
        })
    }

    private fun reloadData() {
        setVisibility(true, false)
        viewModel.loadPostRound(groupIdx)
    }

    private fun setVisibility(beforeLoad: Boolean, isEmpty: Boolean) {
        with(binding) {
            if (beforeLoad) {
                svManageRound.visibility = View.INVISIBLE
                layoutManageNoRound.visibility = View.INVISIBLE
                manageRoundCircularIndicator.visibility = View.VISIBLE
            } else {
                svManageRound.visibility = if (isEmpty) View.INVISIBLE else View.VISIBLE
                layoutManageNoRound.visibility = if (isEmpty) View.VISIBLE else View.INVISIBLE
                manageRoundCircularIndicator.visibility = View.INVISIBLE
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ib_basic_toolbar_back -> finish()
        }
    }
}