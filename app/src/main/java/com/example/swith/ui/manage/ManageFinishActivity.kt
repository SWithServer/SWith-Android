package com.example.swith.ui.manage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.data.ManageUserIdx
import com.example.swith.data.ManageUserProfileResponse
import com.example.swith.data.StudyFinishReq
import com.example.swith.data.StudyFinishResponse
import com.example.swith.databinding.ActivityManageFinishBinding
import com.example.swith.repository.RetrofitApi
import com.example.swith.repository.RetrofitService
import com.example.swith.utils.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ManageFinishActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityManageFinishBinding
    var groupIdx : Int = -1
//    val adminId = SharedPrefManager(this).getLoginData()
//    val adminIdx = adminId?.userIdx

    var adminIdx : Long = 1
    // shared로 바꿔야하는 부분.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_manage_finish)

        initData()
        with(binding)
        {
            btnYes.setOnClickListener{
                finishStudy(adminIdx!!.toLong(),groupIdx.toLong())
            }
            btnNo.setOnClickListener{
                finish()
            }
            clickListener = this@ManageFinishActivity
        }
    }

    fun initData()
    {
        (intent.hasExtra("groupIdx")).let { groupIdx = intent.getIntExtra("groupIdx", 0) }
        Log.e("summer","groupIdx = ${groupIdx}")
    }

    fun finishStudy(adminIdx:Long,groupIdx:Long){
        var reqBody = StudyFinishReq(adminIdx,groupIdx)
        Log.e("req 값","${reqBody.toString()}")
        val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
        retrofitService.endStudy(reqBody).enqueue(object :
            Callback<StudyFinishResponse> {
            override fun onResponse(
                call: Call<StudyFinishResponse>,
                response: Response<StudyFinishResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("summer", "성공${response.toString()}")
                    response.body()?.apply {
                        Log.e("summer 결과값","${this.result}")
                        finish()
                    }
                }
                else {
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                }
            }
            override fun onFailure(call: Call<StudyFinishResponse>, t: Throwable) {
                Log.e("summer", "onFailure t = ${t.toString()}")
                Log.e("summer", "onFailure msg = ${t.message}")
            }
        })
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ib_basic_toolbar_back -> finish()
        }
    }
}