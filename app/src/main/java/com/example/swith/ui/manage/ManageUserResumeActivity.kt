package com.example.swith.ui.manage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.data.ManageUserResponse
import com.example.swith.data.ManageUserResumeReq
import com.example.swith.data.ManageUserResumeResponse
import com.example.swith.databinding.ActivityManageFinishBinding
import com.example.swith.databinding.ActivityManageUserResumeBinding
import com.example.swith.repository.RetrofitApi
import com.example.swith.repository.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageUserResumeActivity:AppCompatActivity(),View.OnClickListener {
    lateinit var binding:ActivityManageUserResumeBinding
    var groupIdx : Long? = -1
    var userIdx : Long? = -1
    var status : Int? = -1
    var resumeIdx : Long?= -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_user_resume)
        binding.clickListener = this
        initData()
        clickButton()
    }

    fun clickButton()
    {
        with(binding)
        {
            btnYes.setOnClickListener {
                postData(1)
                finish()
            }
            btnNo.setOnClickListener {
                postData(2)
                finish()
            }
        }
    }

    fun postData(statusOfApplication : Int)
    {
        Log.e("groupIdx 레트로핏 값 ", "${groupIdx}")
        val resumeReq = ManageUserResumeReq(resumeIdx,1,statusOfApplication)
        val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
        retrofitService.postUserResume(groupIdx!!,0,resumeReq).enqueue(object :
            Callback<ManageUserResumeResponse> {
            override fun onResponse(
                call: Call<ManageUserResumeResponse>,
                response: Response<ManageUserResumeResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("summer", "성공${response.toString()}")
                    response.body()?.apply {
                        Log.e("resume 승인/반려 성공" ,"${this.toString()}")
                    }
                }
                else {
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ManageUserResumeResponse>, t: Throwable) {
                Log.e("summer", "onFailure t = ${t.toString()}")
                Log.e("summer", "onFailure msg = ${t.message}")
            }
        })
    }

    fun setResume()
    {

    }

    fun initData()
    {
        (intent.hasExtra("userIdx")).let { userIdx = intent.getLongExtra("userIdx", 0) }
        Log.e("resume","userIdx = ${userIdx}")
        (intent.hasExtra("status").let{
            status = intent.getIntExtra("status",0)
            Log.e("status값","${status}")
            when(status)
            {
                0->{
                    Log.e("when 0 값","true")
                    with(binding)
                    {
                        btnYes.visibility= View.VISIBLE
                        btnNo.visibility = View.VISIBLE
                    }
                }
                1->{
                    Log.e("when 1 값 ","true")
                    with(binding)
                    {
                        btnYes.visibility= View.GONE
                        btnNo.visibility = View.GONE
                    }
                }
            }
        })
        (intent.hasExtra("groupIdx").let{
            groupIdx = intent.getIntExtra("groupIdx",0).toLong()
            Log.e("groupIdx값","${groupIdx}")
        })
        (intent.hasExtra("resumeIdx").let{
            groupIdx = intent.getIntExtra("resumeIdx",0).toLong()
            Log.e("resumeIdx값","${resumeIdx}")
        })
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ib_basic_toolbar_back -> finish()
        }
    }
}