package com.example.swith.ui.profile

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityProfileModifyBinding
import com.example.swith.databinding.DialogImageBinding
import com.example.swith.databinding.DialogInterestingBinding
import com.example.swith.databinding.DialogProfileBinding
import com.example.swith.ui.MainActivity
import com.example.swith.ui.dialog.CustomDialog
import com.example.swith.ui.dialog.CustomImageDialog
import com.example.swith.ui.dialog.CustomInterestingDialog
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
    }


    private fun initData() {
        //TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.civ_image -> {
                hideKeyboard()
                showImageDialog()
            }
            R.id.tv_location_detail -> {
                //TODO
                hideKeyboard()
            }
            R.id.btn_save -> {
                hideKeyboard()
                if (allCheck()) {
                    showSaveDialog()
                }
            }
            R.id.btn_interesting1 -> {
                hideKeyboard()
                showInterestingDialog(1)
            }
            R.id.btn_interesting2 -> {
                //TODO
                hideKeyboard()
                showInterestingDialog(2)
            }
            R.id.tv_interesting1 -> {
                hideKeyboard()
                //TODO
            }
            R.id.tv_interesting2 -> {
                hideKeyboard()
                //TODO
            }
        }
    }

    private fun showImageDialog() {
        DataBindingUtil.inflate<DialogImageBinding>(
            LayoutInflater.from(this@ProfileModifyActivity),
            R.layout.dialog_image,
            null,
            false
        ).apply {
            this.imageDialog = CustomBinder.showCustomImageDialog(
                this@ProfileModifyActivity,
                root,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                object : CustomImageDialog.DialogClickListener {
                    override fun onClose() {
                        // nothing
                        Log.e("doori", "onClose")
                    }

                    override fun onCamera() {
                        Log.e("doori", "camera")
                        Toast.makeText(this@ProfileModifyActivity, "camera", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onGallery() {
                        Log.e("doori", "gallery")
                        Toast.makeText(this@ProfileModifyActivity, "camera", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }
    }

    private fun showInterestingDialog(btnNumber: Int) {
        val imageArray = resources.getStringArray(R.array.intersting).toList() as ArrayList<String>
        DataBindingUtil.inflate<DialogInterestingBinding>(
            LayoutInflater.from(this@ProfileModifyActivity),
            R.layout.dialog_interesting,
            null,
            false
        ).apply {
            val interestingDialog = CustomBinder.showCustomInterestringDialog(imageArray,
                binding.btnInteresting1.text as String,
                binding.btnInteresting2.text as String,
                this@ProfileModifyActivity,
                root,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                object : CustomInterestingDialog.DialogClickListener {
                    override fun onSelect(select: String) {
                        Log.e("doori", "onSelect = $select")
                        if (btnNumber == 1) {
                            binding.btnInteresting1.text = select
                        } else if (btnNumber == 2) {
                            binding.btnInteresting2.text = select
                        }

                    }

                    override fun onClose() {
                        //nothing
                        Log.e("doori", "onclose")
                    }

                })
            this.interestingDialog = interestingDialog
            val interestingAdapter = interestingDialog.getAdapter()
            this.rvList.adapter = interestingAdapter
        }
    }

    private fun showSaveDialog() {
        DataBindingUtil.inflate<DialogProfileBinding>(
            LayoutInflater.from(this@ProfileModifyActivity),
            R.layout.dialog_profile,
            null,
            false
        ).apply {
            this.profileDialog = CustomBinder.showCustomDialog(
                this@ProfileModifyActivity,
                root,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                object : CustomDialog.DialogClickListener {
                    override fun onClose() {
                        // nothing
                    }

                    override fun onConfirm() {
                        goProfilePage()
                    }
                })
        }
    }

    fun goProfilePage() {
        Intent(this@ProfileModifyActivity, MainActivity::class.java).run {
            putExtra("profile", "ProfileFragment")
            startActivity(this)
        }
    }

    fun allCheck(): Boolean {
        binding.apply {
            if (etNickname.text.isEmpty()) {
                showDialog("닉네임을 입력해주세요.")
                return false
            }
            if (btnInteresting1.text.toString() == "+" && btnInteresting2.text.toString() == "+") {
                showDialog("관심분야를 선택해주세요.")
                return false
            }
            if (tvLocationDetail.text.toString() == "선택해주세요j.") {
                showDialog("활동지역을 선택해주세요.")
                return false
            }
            if (etIntroduceDetail.text.isEmpty()) {
                showDialog("소개글을 작성해주세요.")
                return false
            }
            return true
        }
    }

    fun showDialog(errorMsg: String) {
        val builder = AlertDialog.Builder(this@ProfileModifyActivity)
            .setTitle("프로필을 모두 작성해주세요.")
            .setMessage(errorMsg)
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { _, _ ->
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { _, _ ->

                })
        builder.show()


    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = window.currentFocus
        if (currentFocus != null) {
            inputManager.hideSoftInputFromWindow(
                currentFocus.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}