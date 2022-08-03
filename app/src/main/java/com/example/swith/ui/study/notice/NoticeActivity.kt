package com.example.swith.ui.study.notice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityNoticeBinding
import com.example.swith.utils.NoticeManager

class NoticeActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityNoticeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notice)

        initToolbar()

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener=this@NoticeActivity
    }

    private fun initData() {
        //공지사항 가져오기
    }

    private fun initToolbar(){
        setSupportActionBar(binding.studyNoticeToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_test ->{
                //TODO only test, pushService만들면 로직 삭제
                NoticeManager(this@NoticeActivity).runNotice("알람제목","알람테스트")
            }
        }
    }
}