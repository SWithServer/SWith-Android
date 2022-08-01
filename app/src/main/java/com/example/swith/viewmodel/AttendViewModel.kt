package com.example.swith.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swith.data.UserAttend
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class AttendViewModel : ViewModel() {
    private var _attendLiveData = MutableLiveData<ArrayList<UserAttend>>()
    private var _userIdx = -1
    private var _attendLimit = 10

    private var attendList = ArrayList<UserAttend>()
    // 출석 유효 시간
    val attendLimit : Int
        get() = _attendLimit

    val attendLiveData: LiveData<ArrayList<UserAttend>>
        get() = _attendLiveData


    init {
        // repository 에서 받아오는 부분으로 수정될 예정
        attendList.add(UserAttend(1, "개똥", 1))
        attendList.add(UserAttend(2, "철수", 3))
        attendList.add(UserAttend(3, "영희", 2))
        attendList.add(UserAttend(4,"민우", 3))
        attendList.add(UserAttend(5, "은지", 0))
        attendList.sortWith(compareBy{it.name})

        _attendLiveData.value = attendList
    }

    // 현재 유저의 아이디를 통해 현재 유저가 누구인지 set
    fun setCurUser(userId : Int) : Boolean{
        attendList?.let {
            for (i in 0 until attendList.size) {
                if (userId == attendList[i].userId){
                    _userIdx = i
                    return true
                }
            }
        }
        return false
    }

    fun isUpdateAvailable() : Boolean{
        // Todo: 시간 조건 비교하는 것 추가
        if (_attendLiveData.value?.get(_userIdx)?.attend == 0) return true
        return false
    }
    // 현재 유저 출석 업데이트
    fun updateCurAttend(){
        (_userIdx != -1).let {
            attendList[_userIdx].attend = 1
            _attendLiveData.value = attendList
//            viewModelScope.launch{
//
//            }
        }
    }


}