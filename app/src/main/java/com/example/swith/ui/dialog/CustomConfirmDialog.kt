package com.example.swith.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.swith.R
import com.example.swith.databinding.DialogConfirmBinding

class CustomConfirmDialog(private val title: String, private val content: String) : DialogFragment()  {
    private lateinit var binding: DialogConfirmBinding

    interface CustomListener{
        fun onConfirm()
    }

    private lateinit var customListener: CustomListener

    fun setCustomListener(listener: CustomListener){
        customListener = listener
    }

    override fun onResume() {
        super.onResume()

        // 디바이스 크기별 세팅
        val params = dialog?.window?.attributes
        val windowManager = activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = windowManager.currentWindowMetrics
        val deviceWidth = size.bounds.width()

        params?.width = (deviceWidth * 0.8).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_confirm, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            tvConfirmTitle.text = title
            tvConfirmContent.text = content
            tvDialogCancel.setOnClickListener { dismiss() }
            btnDialogConfirm.setOnClickListener { customListener.onConfirm() }
        }
    }

}