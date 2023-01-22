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
import com.example.data.databinding.DialogAnnounceModifyBinding

class CustomAnnounceModifyDialog(private val content: String) : DialogFragment() {
    private lateinit var binding: DialogAnnounceModifyBinding

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
            DataBindingUtil.inflate(inflater, R.layout.dialog_announce_modify, container, false)
        // 모서리 직각 제거
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etAnnounceModify.setText(content)
        binding.etAnnounceModify.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.btnAnnounceModify.visibility = View.INVISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnAnnounceModify.visibility =
                    if (s.isNullOrEmpty() || s.toString() == content) View.INVISIBLE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                // 없음
            }

        })
        binding.btnAnnounceModify.setOnClickListener { customListener.onConfirm(binding.etAnnounceModify.text.toString()) }
        binding.tvDialogModifyCancel.setOnClickListener { dismiss() }
    }
}