package com.example.swith.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager

class CustomImageDialog(context: Context, view: View, width: Int = WindowManager.LayoutParams.WRAP_CONTENT, height: Int = WindowManager.LayoutParams.WRAP_CONTENT): Dialog(context) {
    private var listener: DialogClickListener? = null
    private var isClickConfirm: Boolean = false

    init {
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
        fun onCamera()
        fun onGallery()
    }

    fun setClickListener(listener: DialogClickListener?) {
        this.listener = listener
    }
    fun onClose() {
        dismiss()
    }

    fun onCamera() {
        listener?.onCamera()
    }
    fun onGallery(){
        listener?.onGallery()
    }
}