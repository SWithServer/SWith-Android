package com.example.swith.ui.profile

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.swith.R
import com.example.swith.data.ProfileModifyRequest
import com.example.swith.data.ProfileRequest
import com.example.swith.data.ProfileResponse
import com.example.swith.databinding.ActivityProfileModifyBinding
import com.example.swith.databinding.DialogImageBinding
import com.example.swith.databinding.DialogInterestingBinding
import com.example.swith.databinding.DialogProfileBinding
import com.example.swith.ui.MainActivity
import com.example.swith.ui.dialog.CustomDialog
import com.example.swith.ui.dialog.CustomImageDialog
import com.example.swith.ui.dialog.CustomInterestingDialog
import com.example.swith.ui.study.create.SelectPlaceActivity
import com.example.swith.utils.CustomBinder
import com.example.swith.utils.SharedPrefManager
import com.example.swith.viewmodel.ProfileModifyViewModel
import com.google.android.datatransport.cct.internal.LogEvent

class ProfileModifyActivity : AppCompatActivity(), View.OnClickListener, Observer<ProfileResponse> {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                //nothing
            } else {
                Toast.makeText(this@ProfileModifyActivity, "권한을 허용해주세요", Toast.LENGTH_SHORT).show()
            }
        }

    private val requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        if (it.resultCode == RESULT_OK) {
            Glide.with(this)
                .load(it.data!!.data)
                .into(binding.civProfile)
        }
    }

    private val requestCameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        if (it.resultCode == RESULT_OK) {
            Glide.with(this)
                .load(it.data!!.extras?.get("data"))
                .into(binding.civProfile)
        }
    }

    private var mProfileModifyViewModel: ProfileModifyViewModel? = null
    lateinit var binding: ActivityProfileModifyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_modify)

        initData()
        initView()
    }

    private fun initView() {
        setShowDimmed(true)

        binding.clickListener = this@ProfileModifyActivity
        binding.apply {
            lifecycleOwner = this@ProfileModifyActivity
            mProfileModifyViewModel =
                ViewModelProvider(this@ProfileModifyActivity, ProfileModifyViewModel.Factory()).get(ProfileModifyViewModel::class.java).apply {
                    profileModifyViewModel = this
                    getCurrentProfile().observe(this@ProfileModifyActivity, this@ProfileModifyActivity)
                    SharedPrefManager(this@ProfileModifyActivity).getLoginData()?.userIdx!!.apply {
                        requestCurrentProfile(ProfileRequest(this as Long))
                    }
                }
            mProfileModifyViewModel?.getCurrentProfileModify()?.observe(this@ProfileModifyActivity, Observer {
                Log.e("doori", "observer = $it")
                //TODO 관심분야가 index4번이상부터 안되넹
                setShowDimmed(false)
                if (it != null) {
                    if (it!!.isSuccess) {
                        goProfilePage()
                    } else {
                        Toast.makeText(this@ProfileModifyActivity, "잠시 후 다시 시작해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        }
    }


    private fun initData() {

    }

    override fun onResume() {
        super.onResume()
        if(!getSharedPreferences("result4",0).getString("이름4","").toString().equals("")) {
            val shPref1 = getSharedPreferences("result4",0)
            val editor1 = getSharedPreferences("result4",0).edit()
            binding.tvLocationDetail.text = shPref1.getString("이름4", "")
        }
    }
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.civ_image -> {
                hideKeyboard()
                showImageDialog()
            }
            R.id.tv_location_detail -> {
                //TODO 서머님께 부탁 ㅜㅜ
                Log.e("doori","tv_location_click")
                Intent(this@ProfileModifyActivity,SelectPlaceActivity::class.java).run {
                    this.putExtra("번호",4)
                    startActivity(this)
              }
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
                hideKeyboard()
                showInterestingDialog(2)
            }
        }
    }

    private fun showImageDialog() {
        DataBindingUtil.inflate<DialogImageBinding>(
            LayoutInflater.from(this@ProfileModifyActivity), R.layout.dialog_image, null, false
        ).apply {
            this.imageDialog = CustomBinder.showCustomImageDialog(this@ProfileModifyActivity,
                root,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                object : CustomImageDialog.DialogClickListener {
                    override fun onClose() {
                        // nothing
                    }

                    override fun onCamera() {
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                        getImageToCamera()
                    }

                    override fun onGallery() {
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        getImageToGallery()
                    }
                })
        }
    }

    private fun getImageToCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).run {
            requestCameraLauncher.launch(this)
        }
    }

    private fun getImageToGallery() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).run {
            type = "image/*"
            requestGalleryLauncher.launch(this)

        }
    }

    private fun showInterestingDialog(btnNumber: Int) {
        val imageArray = resources.getStringArray(R.array.intersting).toList() as ArrayList<String>
        DataBindingUtil.inflate<DialogInterestingBinding>(
            LayoutInflater.from(this@ProfileModifyActivity), R.layout.dialog_interesting, null, false
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
            LayoutInflater.from(this@ProfileModifyActivity), R.layout.dialog_profile, null, false
        ).apply {
            this.profileDialog = CustomBinder.showCustomDialog(this@ProfileModifyActivity,
                root,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                object : CustomDialog.DialogClickListener {
                    override fun onClose() {
                        // nothing
                    }

                    override fun onConfirm() {
                        setShowDimmed(true)
                        requestProfile()
                    }
                })
        }
    }

    private fun requestProfile() {
        val email = mProfileModifyViewModel?.getCurrentProfile()?.value?.result!!.email

        binding.apply {
            Log.e(
                "doori",
                "${
                    ProfileModifyRequest(
                        email,
                        getInterestringIndex(btnInteresting1.text.toString()),
                        getInterestringIndex(btnInteresting2.text.toString()),
                        etIntroduceDetail.text.toString(),
                        etNickname.text.toString(),
                        tvLocationDetail.text.toString()
                    )
                }"
            )
            mProfileModifyViewModel?.requestCurrentProfileModify(
                ProfileModifyRequest(
                    email,
                    getInterestringIndex(btnInteresting1.text.toString()),
                    getInterestringIndex(btnInteresting2.text.toString()),
                    etIntroduceDetail.text.toString(),
                    etNickname.text.toString(),
                    tvLocationDetail.text.toString()
                )
            )
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
            if (tvLocationDetail.text.toString() == "지역을 선택해주세요.") {
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
        val builder = AlertDialog.Builder(this@ProfileModifyActivity).setTitle("프로필을 모두 작성해주세요.").setMessage(errorMsg)
            .setPositiveButton("확인", DialogInterface.OnClickListener { _, _ ->
            }).setNegativeButton("취소", DialogInterface.OnClickListener { _, _ ->

            })
        builder.show()
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = window.currentFocus
        if (currentFocus != null) {
            inputManager.hideSoftInputFromWindow(
                currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    override fun onChanged(profileResponse: ProfileResponse?) {
        setShowDimmed(false)
        Log.e("doori", "onChanged = ${profileResponse.toString()}")
        //TODO 관심분야가 없다면?? 널값은 어떻게 처리해줘야할까?
        profileResponse?.result?.apply {
            binding.btnInteresting1.text = resources.getStringArray(R.array.intersting)[interestIdx1]
            binding.btnInteresting2.text = resources.getStringArray(R.array.intersting)[interestIdx2]
        }
    }

    private fun setShowDimmed(isLoading: Boolean) {
        mProfileModifyViewModel?.apply {
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mProfileModifyViewModel?.run {
            getCurrentProfile().removeObserver(this@ProfileModifyActivity)
        }
    }

    private fun getInterestringIndex(interesting: String): Int {
        if (interesting == "자격증/시험") {
            return 1
        } else if (interesting == "어학") {
            return 2
        } else if (interesting == "청소년/입시") {
            return 3
        } else if (interesting == "취업/창업") {
            return 4
        } else if (interesting == "컴퓨터/IT") {
            return 5
        } else if (interesting == "취미/문화") {
            return 6
        } else if (interesting == "면접") {
            return 7
        } else {
            return 8
        }
    }
}

