package com.example.swith.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.data.ui.adapter.InterestingAdapter

class CustomInterestingDialog(
    dataList: ArrayList<String>,
    interesting1: String,
    interesting2: String,
    context: Context,
    view: View,
    width: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    height: Int = WindowManager.LayoutParams.WRAP_CONTENT
) : Dialog(context) {
    private var listener: DialogClickListener? = null
    private var clickConfirm: Boolean = false
    private var select: String = ""
    private var interesting1: String = ""
    private var interesting2: String = ""
    private var dataList: ArrayList<String>? = null
    private var interestingAdapter: InterestingAdapter? = null

    init {
        this.interesting1 = interesting1
        this.interesting2 = interesting2

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCanceledOnTouchOutside(false)
        setContentView(view)
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(width, height)
        }

        setOnDismissListener {
            if (!clickConfirm) {
                listener?.onClose()
            }
        }
        this.dataList = dataList

        setAdapter()
    }

    interface DialogClickListener {
        fun onSelect(select: String)
        fun onClose()
    }

    fun setClickListener(listener: DialogClickListener?) {
        this.listener = listener
    }

    fun onClose() {
        dismiss()
    }

    fun setAdapter() {
        interestingAdapter = InterestingAdapter(object : InterestingAdapter.InterestingCallback {
            override fun onClick(data: String) {
                select = data
                listener?.onSelect(select)
                dismiss()
            }
        })
        Log.e("doori", dataList.toString())
        dataList.apply {
            this?.let { interestingAdapter?.setDataList(it, interesting1, interesting2) }
        }
    }

    fun getAdapter(): InterestingAdapter? = interestingAdapter

}