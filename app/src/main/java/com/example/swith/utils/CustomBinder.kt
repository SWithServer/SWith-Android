package com.example.swith.utils

import android.view.View
import androidx.databinding.BindingAdapter

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
    }
}