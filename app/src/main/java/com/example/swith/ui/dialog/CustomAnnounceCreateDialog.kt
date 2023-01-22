package com.example.swith.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.data.R
import com.example.data.databinding.DialogAnnounceCreateBinding

class CustomAnnounceCreateDialog : DialogFragment() {
    private lateinit var binding: DialogAnnounceCreateBinding

    interface CustomListener {
        fun onConfirm(content: String)
    }

    private lateinit var customListener: CustomListener
    fun setCustomListener(listener: CustomListener) {
        customListener = listener
    }

    override fun onResume() {
        super.onResume()
        // 디바이스 크기별 세팅
        val params = dialog?.window?.attributes
        val windowManager = activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = windowManager.currentWindowMetrics
        val deviceWidth = size.bounds.width()
        val deviceHeight = size.bounds.height()

        params?.width = (deviceWidth * 0.8).toInt()
        params?.height = (deviceHeight * 0.7).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_announce_create, container, false)
        // 모서리 직각 제거
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etAnnounceCreate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.btnAnnounceCreate.visibility = View.INVISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnAnnounceCreate.visibility =
                    if (!s.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                // 행동 X
            }

        })
        binding.btnAnnounceCreate.setOnClickListener { customListener.onConfirm(binding.etAnnounceCreate.text.toString()) }
    }
}