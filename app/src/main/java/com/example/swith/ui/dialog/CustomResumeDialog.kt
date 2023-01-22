package com.example.swith.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager

class CustomResumeDialog(
    title: String,
    context: Context,
    view: View,
    width: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
) : Dialog(context) {
    private var listener: DialogClickListener? = null
    private var isClickConfirm: Boolean = false
    private var title: String = ""

    init {
        this.title = title
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCanceledOnTouchOutside(false)
        setContentView(view)
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(width, height)
        }

        setOnDismissListener {
            if (!isClickConfirm) listener?.onClose()
        }
    }

    interface DialogClickListener {
        fun onClose()
        fun onConfirm()
    }

    fun setClickListener(listener: DialogClickListener?) {
        this.listener = listener
    }

    fun onClose() {
        dismiss()
    }

    fun onConfirm() {
        isClickConfirm = true
        dismiss()
        listener?.onConfirm()
    }

    fun getTitle(): String {
        return title
    }
}