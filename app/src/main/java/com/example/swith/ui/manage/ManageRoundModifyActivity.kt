package com.example.swith.ui.manage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.data.entity.GetSessionRes
import com.example.swith.R
import com.example.swith.domain.entity.DateTime
import com.example.swith.domain.entity.SessionModify
import com.example.swith.ui.dialog.BottomSheet
import com.example.swith.ui.dialog.CustomAlertDialog
import com.example.swith.ui.dialog.CustomConfirmDialog
import com.example.swith.ui.study.create.RoundCreateActivity

class ManageRoundModifyActivity : RoundCreateActivity(), View.OnClickListener {
    private val curRound: GetSessionRes by lazy {
        intent.getSerializableExtra("curRound") as GetSessionRes
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        observeViewModel()
    }

    private fun initView() {
        with(binding) {
            tvCreateInfo.visibility = View.INVISIBLE
            binding.toolbarCreate.tvRoundTitle.apply {
                visibility = View.VISIBLE
                text = "${curRound.sessionNum}회차"
            }
            startTime = DateTime(
                curRound.sessionStart[0],
                curRound.sessionStart[1],
                curRound.sessionStart[2],
                curRound.sessionStart[3],
                curRound.sessionStart[4]
            )
            endTime = DateTime(
                curRound.sessionEnd[0],
                curRound.sessionEnd[1],
                curRound.sessionEnd[2],
                curRound.sessionEnd[3],
                curRound.sessionEnd[4]
            )
            btnCreateStartDate.text =
                "시작 : ${startTime?.year}.${startTime?.month}.${startTime?.day} ${startTime?.hourOfDay}:${startTime?.minute}"
            btnCreateEndDate.text =
                "종료 : ${endTime?.year}.${endTime?.month}.${endTime?.day} ${endTime?.hourOfDay}:${endTime?.minute}"
            if (curRound.online == 1) {
                btnCreateOnline.isSelected = true
                etCreatePlace.setText("온라인")
            } else {
                btnCreateOffline.isSelected = true
                etCreatePlace.setText(curRound.place)
            }
            etCreateDetail.setText(curRound.sessionContent)
            btnCreateAdd.text = "수정 완료"
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ib_round_toolbar_back -> {
                CustomConfirmDialog("수정 취소", "변경 내용이 저장되지 않았습니다. 그래도 종료하시겠습니까?").apply {
                    setCustomListener(object : CustomConfirmDialog.CustomListener {
                        override fun onConfirm() {
                            finish()
                        }
                    })
                }.show(supportFragmentManager, "회차 수정 취소")
            }
        }
    }


    override fun onBackPressed() {
        CustomConfirmDialog("수정 취소", "변경 내용이 저장되지 않았습니다. 그래도 종료하시겠습니까?").apply {
            setCustomListener(object : CustomConfirmDialog.CustomListener {
                override fun onConfirm() {
                    finish()
                }
            })
        }.show(supportFragmentManager, "회차 수정 취소")
    }

    override fun initListener() {
        super.initListener()
        binding.btnCreateAdd.setOnClickListener {
            BottomSheet(
                "${curRound.sessionNum}회차 수정",
                null,
                resources.getString(R.string.manage_round_modify),
                "수정"
            ).apply {
                setCustomListener(object : BottomSheet.customClickListener {
                    override fun onCheckClick() {
                        val online = if (binding.btnCreateOnline.isSelected) 1 else 0
                        val startTimeToString: String = String.format(
                            "%4d-%02d-%02dT%02d:%02d",
                            startTime?.year,
                            startTime?.month,
                            startTime?.day,
                            startTime?.hourOfDay,
                            startTime?.minute
                        )
                        val endTimeToString: String = String.format(
                            "%4d-%02d-%02dT%02d:%02d",
                            endTime?.year,
                            endTime?.month,
                            endTime?.day,
                            endTime?.hourOfDay,
                            endTime?.minute
                        )
                        viewModel.modifyRound(
                            SessionModify(
                                online,
                                binding.etCreatePlace.text.toString(),
                                binding.etCreateDetail.text.toString(),
                                endTimeToString,
                                curRound.sessionIdx,
                                startTimeToString
                            )
                        )
                    }
                })
            }.show(supportFragmentManager, "회차 수정 취소")
        }
    }

    override fun observeViewModel() {
        viewModel.sessionLiveEvent.observe(this, Observer {
            Toast.makeText(applicationContext, "회차 수정이 완료되었습니다", Toast.LENGTH_SHORT).show()
            finish()
        })
        viewModel.mutableErrorMessage.observe(this, Observer {
            CustomAlertDialog("회차 수정 오류", it.toString()).show(supportFragmentManager, "회차 수정 오류")
        })
    }
}