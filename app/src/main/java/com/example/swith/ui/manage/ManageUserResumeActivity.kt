package com.example.swith.ui.manage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.api.SwithService
import com.example.swith.databinding.ActivityManageUserResumeBinding
import com.example.swith.repository.RetrofitService
import com.example.swith.utils.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageUserResumeActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityManageUserResumeBinding
    var userIdx: Long? = -1
    var groupIdx: Long? = -1

//    val adminId = SharedPrefManager(this@ManageUserResumeActivity).getLoginData()
//    val adminIdx = adminId?.userIdx

    //    val adminIdx : Long = 1
    var applicationIdx: Long? = -1

    var status: Int? = -1
    var applicationContent: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_user_resume)
        binding.clickListener = this
        initData()
        binding.flLoadingLayout.visibility = View.VISIBLE
        setResume(applicationContent)
        clickButton()
    }

    fun clickButton() {
        with(binding)
        {
            btnYes.setOnClickListener {
                //승인
                postData(1)
                finish()
            }
            btnNo.setOnClickListener {
                //반려
                postData(2)
                finish()
            }
        }
    }

    fun postData(statusOfApplication: Int) {
        Log.e("groupIdx 레트로핏 값 ", "${groupIdx}")
        Log.e("applicationIdx 신청서 idx 값", "${applicationIdx}")
        val resumeReq = com.example.swith.entity.ManageUserResumeReq(
            applicationIdx,
            SharedPrefManager(this@ManageUserResumeActivity).getLoginData()!!.userIdx,
            statusOfApplication
        )
        Log.e("req 부분", "${resumeReq}")
        val retrofitService = RetrofitService.retrofit.create(SwithService::class.java)
        retrofitService.postUserResume(groupIdx!!, 0, resumeReq).enqueue(object :
            Callback<com.example.swith.entity.ManageUserResumeResponse> {
            override fun onResponse(
                call: Call<com.example.swith.entity.ManageUserResumeResponse>,
                response: Response<com.example.swith.entity.ManageUserResumeResponse>
            ) {
                if (response.isSuccessful) {
                    binding.flLoadingLayout.visibility = View.GONE
                    Log.e("summer", "성공${response.toString()}")
                    response.body()?.apply {
                        Log.e("resume 승인/반려 성공", "${this.toString()}")
                    }
                } else {
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                }
            }

            override fun onFailure(
                call: Call<com.example.swith.entity.ManageUserResumeResponse>,
                t: Throwable
            ) {
                Log.e("summer", "onFailure t = ${t.toString()}")
                Log.e("summer", "onFailure msg = ${t.message}")
            }
        })
    }

    // resume 값 가져오기
    fun setResume(resumeContent: String?) {
        binding.flLoadingLayout.visibility = View.GONE
        with(binding)
        {
            tvResumeContent.text = resumeContent
        }
    }

    fun initData() {
        (intent.hasExtra("status").let {
            status = intent.getIntExtra("status", 0)
            Log.e("status값", "${status}")
            when (status) {
                0 -> {
                    Log.e("when 0 값", "true")
                    with(binding)
                    {
                        btnYes.visibility = View.VISIBLE
                        btnNo.visibility = View.VISIBLE
                    }
                }
                1 -> {
                    Log.e("when 1 값 ", "true")
                    with(binding)
                    {
                        btnYes.visibility = View.GONE
                        btnNo.visibility = View.GONE
                    }
                }
            }
        })
        (intent.hasExtra("groupIdx").let {
            groupIdx = intent.getIntExtra("groupIdx", 0).toLong()
            Log.e("groupIdx값", "${groupIdx}")
        })
        (intent.hasExtra("applicationIdx").let {
            applicationIdx = intent.getLongExtra("applicationIdx", -1)
            Log.e("applicationIdx값", "${applicationIdx}")
        })
        (intent.hasExtra("applicationContent").let {
            applicationContent = intent.getStringExtra("applicationContent")
            Log.e("지원서 내용:applicationContent 값", "${applicationContent}")
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ib_basic_toolbar_back -> finish()
        }
    }
}