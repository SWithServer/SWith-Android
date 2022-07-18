package com.example.swith.ui.study.create

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityCreateRoundBinding
import com.example.swith.utils.ToolBarManager

class CreateRoundActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateRoundBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_round)

        ToolBarManager(this).initToolBar(binding.toolbarCreate,
            titleVisible = false,
            backVisible = true
        )
        initCheckBox()
        initListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initCheckBox(){
        with(binding){
            etCreatePlace.isEnabled = false
            cbCreateOnline.setOnCheckedChangeListener{ view, isChecked ->
                if (isChecked) {
                    cbCreateOffline.isChecked = false
                    etCreatePlace.apply {
                        isEnabled = false
                        setText("온라인")
                    }
                } else{
                    etCreatePlace.apply{
                        if (cbCreateOffline.isChecked) isEnabled = true
                        hint = "장소(텍스트 입력)"
                        setText("")
                    }
                }
            }
            cbCreateOffline.setOnCheckedChangeListener{ view, isChecked ->
                if (isChecked) {
                    cbCreateOnline.isChecked = false
                    etCreatePlace.apply {
                        isEnabled = true
                    }
                }
                else {
                    etCreatePlace.apply{
                        isEnabled = false
                        setText("")
                    }
                }
            }
        }
    }

    private fun initListener(){
        // 시간 설정 후에 추가해줘야 함
        with(binding){
            etCreateDetail.doAfterTextChanged {
                // 세 개의 뷰 텍스트가 전부 empty가 아닐 때 -> 버튼 활성화
                setAddButton(!it.isNullOrBlank() && !etCreateDetail.text.isNullOrBlank() && btnCreateDate.text.isNotEmpty())
            }
            etCreatePlace.doAfterTextChanged {
                setAddButton(!it.isNullOrBlank() && !etCreateDetail.text.isNullOrBlank() && btnCreateDate.text.isNotEmpty())
            }
            btnCreateAdd.setOnClickListener {
                // Todo : ViewModel 에서 post
            }
        }
    }

    private fun setAddButton(available: Boolean){
        binding.btnCreateAdd.apply {
            visibility = if (available) View.VISIBLE else View.INVISIBLE
        }
    }
}

