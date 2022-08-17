package com.example.swith.ui.manage

import android.os.Bundle
import android.view.MenuItem
import com.example.swith.R
import com.example.swith.data.DateTime
import com.example.swith.data.GetSessionRes
import com.example.swith.ui.dialog.BottomSheet
import com.example.swith.ui.dialog.CustomConfirmDialog
import com.example.swith.ui.study.create.RoundCreateActivity

class ManageRoundModifyActivity : RoundCreateActivity() {
    private val curRound: GetSessionRes by lazy{
        intent.getSerializableExtra("curRound") as GetSessionRes
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView(){
        with(binding){
            tvCreateInfo.text = "* ${curRound.sessionNum}회차 수정"
            startTime = DateTime(curRound.sessionStart[0], curRound.sessionStart[1], curRound.sessionStart[2], curRound.sessionStart[3], curRound.sessionStart[4])
            endTime = DateTime(curRound.sessionEnd[0], curRound.sessionEnd[1], curRound.sessionEnd[2], curRound.sessionEnd[3], curRound.sessionEnd[4])
            btnCreateStartDate.text = "시작 : ${startTime?.year}.${startTime?.month}.${startTime?.day} ${startTime?.hourOfDay}:${startTime?.minute}"
            btnCreateEndDate.text = "종료 : ${endTime?.year}.${endTime?.month}.${endTime?.day} ${endTime?.hourOfDay}:${endTime?.minute}"
            if (curRound.online == 1) {
                cbCreateOnline.isChecked = true
            }
            else {
                cbCreateOffline.isChecked = true
                etCreatePlace.setText(curRound.place)
            }
            etCreateDetail.setText(curRound.sessionContent)
            btnCreateAdd.text = "수정 완료"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                CustomConfirmDialog("수정 취소", "변경 내용이 저장되지 않았습니다. 그래도 종료하시겠습니까?").apply {
                    setCustomListener(object : CustomConfirmDialog.CustomListener {
                        override fun onConfirm() {
                            finish()
                        }
                    })
                }.show(supportFragmentManager, "회차 수정 취소")
            }
        }
        return true
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
            BottomSheet("${curRound.sessionNum}회차 수정", null, resources.getString(R.string.manage_round_modify), "수정 완료").apply {
                setCustomListener(object: BottomSheet.customClickListener{
                    override fun onCheckClick() {
                        finish()
                        // Todo: ViewModel 에서 Patch
                    }
                })
            }.show(supportFragmentManager, "회차 수정 취소") }
    }
}