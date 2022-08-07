package com.example.swith.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityProfileModifyBinding
import com.example.swith.databinding.DialogProfileBinding
import com.example.swith.ui.MainActivity
import com.example.swith.ui.dialog.CustomDialog
import com.example.swith.utils.CustomBinder

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
       // setSpinnerListener()
    }

//    private fun setSpinnerListener() {
//        val interestingList = resources.getStringArray(R.array.intersting)
//        val interestingAdapter = ArrayAdapter(
//            this,
//            android.R.layout.simple_spinner_dropdown_item, interestingList
//        )
//        binding.spInteresting1.adapter = interestingAdapter
//        binding.spInteresting2.adapter = interestingAdapter
//    }

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
                //TODO
            }
            R.id.btn_save->{
                showDialog()
            }
            R.id.btn_interesting1->{
                //TODO
            }
            R.id.btn_interesting2->{
                //TODO
            }
            R.id.tv_interesting1->{
                //TODO
            }
            R.id.tv_interesting2->{
                //TODO
            }
        }
    }

    private fun showDialog() {
        DataBindingUtil.inflate<DialogProfileBinding>(LayoutInflater.from(this@ProfileModifyActivity), R.layout.dialog_profile, null, false).apply {
            this.profileDialog = CustomBinder.showCustomDialog(this@ProfileModifyActivity, root, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, object : CustomDialog.DialogClickListener {
                override fun onClose() {
                    // nothing
                }

                override fun onConfirm() {
                    goProfilePage()
                }
            })
        }
    }

    fun goProfilePage(){
        Intent(this@ProfileModifyActivity,MainActivity::class.java).run {
            putExtra("profile","ProfileFragment")
            startActivity(this)
        }
    }
}