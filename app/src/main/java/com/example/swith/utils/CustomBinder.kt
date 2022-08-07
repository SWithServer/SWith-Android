package com.example.swith.utils

import android.content.Context
import android.view.View
import android.view.WindowManager
import androidx.databinding.BindingAdapter
import com.example.swith.ui.dialog.CustomDialog

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
    }


}