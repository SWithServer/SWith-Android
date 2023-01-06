package com.example.swith.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.swith.R
import com.example.swith.databinding.DialogTimepickerBinding

class CustomTimePickerDialog(context: Context) : DialogFragment() {
    val dialogBinding: DialogTimepickerBinding by lazy {
        DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_timepicker,
            null,
            false
        )
    }

    interface ClickListener {
        fun roundCreate()
    }

    private lateinit var customListener: ClickListener

    fun setCustomListener(listener: ClickListener) {
        customListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 모서리 직각 제거
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialogBinding.root
    }

    override fun onResume() {
        super.onResume()
        // 디바이스 크기별 세팅
        val params = dialog?.window?.attributes
        val windowManager = activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = windowManager.currentWindowMetrics
        val deviceWidth = size.bounds.width()

        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(dialogBinding) {
            btnRoundCreateCancel.setOnClickListener { dismiss() }
            btnRoundCreateConfirm.setOnClickListener { customListener.roundCreate() }
        }
    }

}