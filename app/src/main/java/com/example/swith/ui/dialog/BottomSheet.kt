package com.example.swith.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheet(
    private val title: String,
    private val alert: String?,
    private val content: String,
    private val buttonMessage: String,
) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetBinding

    interface customClickListener {
        fun onCheckClick()
    }

    private lateinit var customListener: customClickListener
    fun setCustomListener(mClickListener: customClickListener) {
        customListener = mClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        with(binding) {
            tvBottomTitle.text = title
            tvBottomAlert.apply {
                alert?.let { text = alert }
                if (alert == null) visibility = View.GONE
            }
            tvBottomGuide.text = content
            btnBottomCheck.text = buttonMessage
            btnBottomCheck.setOnClickListener { customListener.onCheckClick() }
            btnBottomCancel.setOnClickListener { dismiss() }
        }
    }
}