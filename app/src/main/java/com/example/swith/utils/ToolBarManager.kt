package com.example.swith.utils

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.swith.ui.MainActivity

class ToolBarManager(private val context: AppCompatActivity) {
    fun initToolBar(toolbar: Toolbar, titleVisible: Boolean, backVisible: Boolean){
        context.setSupportActionBar(toolbar)
        context.supportActionBar?.setDisplayShowTitleEnabled(titleVisible)
        context.supportActionBar?.setDisplayHomeAsUpEnabled(backVisible)
    }
}