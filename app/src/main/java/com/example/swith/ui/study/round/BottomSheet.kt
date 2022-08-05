package com.example.swith.ui.study.round

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheet(private val count: Int, private val attendLimit: Int, private var isFromMemo: Boolean) : BottomSheetDialogFragment(){
    private lateinit var binding : FragmentBottomSheetBinding

    interface customClickListener{
        fun onCheckClick()
        fun onCancelClick()
    }
    private lateinit var customListener: customClickListener
    fun setCustomListener(mClickListener: customClickListener){
        customListener = mClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet, container, false)
        initView()
        return binding.root
    }

    private fun initView(){
        with(binding){
            if (isFromMemo) {
                tvBottomTitle.text = "${count}회차 메모 수정"
                tvBottomTime.visibility = View.GONE
                tvBottomGuide.text = resources.getString(R.string.bottom_memo_guide)
                btnBottomCheck.text = "수정 완료"
            }
            else {
                tvBottomTitle.text = "${count}회차 출석"
                tvBottomTime.text = "회차 시작 후 ${attendLimit}분 까지 출석 가능"
            }
            btnBottomCheck.setOnClickListener { customListener.onCheckClick() }
            tvBottomCancel.setOnClickListener { customListener.onCancelClick() }
        }
    }
}