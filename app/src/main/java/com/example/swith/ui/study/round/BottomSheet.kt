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

class BottomSheet(private val count: Int, private val attendLimit: Int) : BottomSheetDialogFragment(){
    private lateinit var binding : FragmentBottomSheetBinding
    interface customClickListener{
        fun onAttendClick()
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
            tvBottomRound.text = "${count}회차 출석"
            tvBottomTime.text = "회차 시작 후 ${attendLimit}분 까지 출석 가능"
            btnBottomAttend.setOnClickListener { customListener.onAttendClick() }
            tvBottomCancel.setOnClickListener { customListener.onCancelClick() }
        }
    }
}