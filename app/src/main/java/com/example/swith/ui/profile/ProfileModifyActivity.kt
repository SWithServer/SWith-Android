package com.example.swith.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityProfileModifyBinding
import com.example.swith.ui.MainActivity
import com.example.swith.ui.region.CityActivity

class ProfileModifyActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityProfileModifyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_modify)

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener = this@ProfileModifyActivity
        setSpinnerListener()
    }

    private fun setSpinnerListener() {
        val interestingList = resources.getStringArray(R.array.intersting)
        val interestingAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, interestingList
        )
        binding.spInteresting1.adapter = interestingAdapter
        binding.spInteresting2.adapter = interestingAdapter
    }

    private fun initData() {
        //TODO("Not yet implemented")
        //dongActivity에서 최종 주소를 받아온다
        intent.getStringExtra("region")?.run {
            binding.tvLocationDetail.text= this.split(",")[0]
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv_location_detail -> {
                Intent(this@ProfileModifyActivity,CityActivity::class.java).run {
                    startActivity(this)
                }
            }
            R.id.btn_save->{
                Intent(this@ProfileModifyActivity,MainActivity::class.java).run {
                    putExtra("profile","ProfileFragment")
                    startActivity(this)
                }
            }
        }
    }
}