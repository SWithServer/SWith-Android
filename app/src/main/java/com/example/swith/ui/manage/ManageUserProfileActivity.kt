package com.example.swith.ui.manage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.data.ManageUserIdx
import com.example.swith.data.ManageUserProfileResponse
import com.example.swith.data.StudyDetailResponse
import com.example.swith.databinding.ActivityManageUserProfileBinding
import com.example.swith.repository.RetrofitApi
import com.example.swith.repository.RetrofitService
import com.example.swith.utils.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageUserProfileActivity : AppCompatActivity(), View.OnClickListener{
    var userIdx : Long? = -1

    lateinit var binding : ActivityManageUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_user_profile)
        binding.clickListener = this
        binding.flLoadingLayout.visibility=View.VISIBLE
        initData()
        setData(userIdx)
    }

    fun initView()
    {

    }

    fun initData()
    {
        (intent.hasExtra("userIdx")).let { userIdx = intent.getLongExtra("userIdx", 0) }
        Log.e("summer","userIdx = ${userIdx}")
    }

    fun setData(userIdx:Long?)
    {
        Log.e("userIdx 데이터 set ","${userIdx}")
        Log.e("summer","데이터 set true")
        var reqUserIdx = ManageUserIdx(userIdx)
        val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
        retrofitService.getUserProfile(reqUserIdx).enqueue(object : Callback<ManageUserProfileResponse> {
            override fun onResponse(
                call: Call<ManageUserProfileResponse>,
                response: Response<ManageUserProfileResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("summer", "성공${response.toString()}")
                    response.body()?.apply {
                        var result = this.result
                        with(binding)
                        {
                            tvName.text=result.nickname
                            when(result.interestIdx1.toInt())
                            {
                                1->{tvInteresting1.text ="자격증/시험"}
                                2->{tvInteresting1.text ="어학"}
                                3->{tvInteresting1.text ="청소년/입시"}
                                4->{tvInteresting1.text ="취업/창업"}
                                5->{tvInteresting1.text ="컴퓨터/IT"}
                                6->{tvInteresting1.text ="취미/문화"}
                                7->{tvInteresting1.text ="면접"}
                            }
                            tvIntroduceDetail.text=result.introduction
                            tvRating.text= result.averageStar.toString()
                        }
                    }
                    binding.flLoadingLayout.visibility=View.GONE
                }
                else {
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ManageUserProfileResponse>, t: Throwable) {
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