package com.example.swith.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.swith.ui.dialog.CustomDialog
import com.example.swith.ui.dialog.CustomImageDialog
import com.example.swith.ui.dialog.CustomInterestingDialog
import de.hdodenhof.circleimageview.CircleImageView

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

        @JvmStatic
        fun showCustomImageDialog(context: Context, view: View, width: Int = WindowManager.LayoutParams.WRAP_CONTENT, height: Int = WindowManager.LayoutParams.WRAP_CONTENT, listener: CustomImageDialog.DialogClickListener?): CustomImageDialog {
            return CustomImageDialog(context, view, width, height).apply {
                setClickListener(listener)
                show()
            }
        }

        @JvmStatic
        @BindingAdapter("app:imageUrl","app:placeholder")
        fun loadImage(imageView: CircleImageView, url: String?, placeholder: Drawable){
            Glide.with(imageView.context)
                .load(url)
                .placeholder(placeholder)
                .error(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(RequestOptions().fitCenter())
                .into(imageView)
        }
    }
}