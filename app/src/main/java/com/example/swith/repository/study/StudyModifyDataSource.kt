package com.example.swith.repository.study

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.data.StudyDetailResponse
import com.example.swith.data.StudyDetailResult
import com.example.swith.data.StudyGroup
import com.example.swith.data.StudyModifyResponse
import com.example.swith.repository.RetrofitService.retrofitApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudyModifyDataSource {
    private var groupIdx : Long = -1
    private var studyDetailLiveData: MutableLiveData<StudyDetailResponse> = MutableLiveData<StudyDetailResponse>()

    fun getStudyInfo(groupIdx : Long) :LiveData<StudyDetailResponse>{
        retrofitApi.getStudyDetail(groupIdx).enqueue(object: Callback<StudyDetailResponse>{
            override fun onResponse(
                call: Call<StudyDetailResponse>,
                response: Response<StudyDetailResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.apply{
                        Log.e("summer","onResponse")
                        studyDetailLiveData.postValue(this)
                    }
                }
                else{
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StudyDetailResponse>, t: Throwable) {
                Log.e("summer","onFailure = $t")
            }
        })
        return studyDetailLiveData
    }

    fun modifyStudy(groupIdx:Long, studyInfo:StudyGroup) : Long{
        retrofitApi.modifyStudy(groupIdx, studyInfo).enqueue(object: Callback<StudyModifyResponse>{
            override fun onResponse(
                call: Call<StudyModifyResponse>,
                response: Response<StudyModifyResponse>
            ) {
               if (response.isSuccessful){
                   response.body()?.apply {
                      this@StudyModifyDataSource.groupIdx = this.result
                   }
               }
                else{
                   Log.e("summer", "전달실패 code = ${response.code()}")
                   Log.e("summer", "전달실패 msg = ${response.message()}")
               }
            }

            override fun onFailure(call: Call<StudyModifyResponse>, t: Throwable) {
                Log.e("summer","onFailure t = $t")
            }
        })
        return groupIdx
    }
}