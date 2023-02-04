package com.example.swith.repository.study

import androidx.lifecycle.MutableLiveData
import com.example.swith.domain.entity.Result

class StudyFindRepository(private val remoteDataSource:StudyFindDataSource) {

    fun loadData(title:String?,region:String?,interest1:Int?,
                         interest2:Int?, sort:Int): MutableLiveData<Result>{
        return remoteDataSource.loadData(title,region,interest1,interest2,sort)
    }

    fun loadMoreData(title:String?,region:String?,interest1:Int?,
                     interest2:Int?, sort:Int,groupIdx:Long?) : MutableLiveData<Result>{
        return remoteDataSource.loadMoreData(title,region,interest1,interest2,sort,groupIdx)
    }
}