package com.example.swith.utils

import android.content.Context
import android.view.View
import android.view.WindowManager
import androidx.databinding.BindingAdapter
import com.example.swith.ui.dialog.CustomDialog
import com.example.swith.ui.dialog.CustomInterestingDialog

class CustomBinder {

    companion object {
        @JvmStatic
        @BindingAdapter("visibleGone")
        fun setVisibility(view: View, visible: Boolean) {
            view.visibility = if (visible) View.VISIBLE else View.GONE
        }

        @JvmStatic
        @BindingAdapter("visibleInvisible")
        fun setVisibility2(view: View, visible: Boolean) {
            view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        }

        @JvmStatic
        fun showCustomDialog(context: Context, view: View, width: Int = WindowManager.LayoutParams.WRAP_CONTENT, height: Int = WindowManager.LayoutParams.WRAP_CONTENT, listener: CustomDialog.DialogClickListener?): CustomDialog {
            return CustomDialog(context, view, width, height).apply {
                setClickListener(listener)
                show()
            }
        }

        @JvmStatic
        fun showCustomInterestringDialog(arrayList: ArrayList<String>,interesting1: String,interesting2: String,context: Context, view: View, width: Int = WindowManager.LayoutParams.WRAP_CONTENT, height: Int = WindowManager.LayoutParams.WRAP_CONTENT, listener: CustomInterestingDialog.DialogClickListener?): CustomInterestingDialog {
            return CustomInterestingDialog(arrayList,interesting1,interesting2,context, view, width, height).apply {
                setClickListener(listener)
                show()
            }
        }
    }


}