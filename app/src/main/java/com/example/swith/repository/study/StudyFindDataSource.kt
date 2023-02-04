package com.example.swith.repository.study

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.swith.data.api.RetrofitService
import com.example.swith.data.api.RetrofitService.retrofitApi
import com.example.swith.data.api.SwithService
import com.example.swith.domain.entity.Result
import com.example.swith.domain.entity.StudyFindReq
import com.example.swith.domain.entity.StudyFindResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StudyFindDataSource {

    private val limit =5
    private var _returnData = MutableLiveData<Result>().apply{
        value = null
    }

    fun loadData(title:String?,region:String?, interest1:Int?, interest2:Int?, sort:Int) : MutableLiveData<Result>{
        val req = StudyFindReq(title,region, null,interest1, interest2,sort, LocalDateTime.now())
        retrofitApi.getSearchStudy(limit,title,region,interest1,interest2,null,sort,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .enqueue(object : Callback<StudyFindResponse> {
            override fun onResponse(
                call: Call<StudyFindResponse>,
                response: Response<StudyFindResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("summer", "성공${response}")
                    response.body()?.apply {
                        Log.e("summer", this.result.content.toString())
                        Log.e("summer", this.result.toString())
                        _returnData.value=this.result
                    }
                }
                else {
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                    Log.e("summer", req.toString())
                }
            }
            override fun onFailure(call: Call<StudyFindResponse>, t: Throwable) {
                Log.e("summer", "onFailure t = ${t.toString()}")
                Log.e("summer", "onFailure msg = ${t.message}")
            }
        })
        return _returnData
    }

    //리사이클러뷰에 더 보여줄 데이터를 로드하는 경우
    fun loadMoreData (title:String?,region:String?, interest1:Int?,
                     interest2:Int?, sort:Int,groupIdx:Long?): MutableLiveData<Result>
    {
        Log.e("loadMore","true")
        var req = StudyFindReq(title,region, groupIdx,interest1, interest2,sort, LocalDateTime.now())
        val retrofitService = RetrofitService.retrofit.create(SwithService::class.java)
        retrofitService.getSearchStudy(limit,title,region,interest1,interest2,groupIdx,sort,
                LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .enqueue(object : Callback<StudyFindResponse> {
                    override fun onResponse(call: Call<StudyFindResponse>, response: Response<StudyFindResponse>) {
                        val body = response.body()
                        if (body != null && response.isSuccessful) {
                            Log.e("groupIdx 데이터 더 받을때","$groupIdx")
                            Log.e("summer","${body.result.content}")
                            Log.e("body last 값", "${body.result.last}")
                        } else {
                            Log.e("summer", "전달실패 code = ${response.code()}")
                            Log.e("summer", "전달실패 msg = ${response.message()}")
                            Log.e("summer", req.toString())
                        }
                    }
                    override fun onFailure(call: Call<StudyFindResponse>, t: Throwable) {
                        Log.e("summer", "onFailure t = $t")
                        Log.e("summer", "onFailure msg = ${t.message}")
                    }
                })
        return _returnData
    }
}