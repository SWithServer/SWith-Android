package com.example.swith.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.swith.data.GetAttendance
import com.example.swith.data.GetSessionRes
import com.example.swith.data.Round
import com.example.swith.data.SessionInfo
import com.example.swith.repository.round.RoundRemoteDataSource
import com.example.swith.repository.round.RoundRepository
import com.example.swith.utils.SingleLiveEvent
import com.example.swith.utils.base.BaseViewModel
import com.example.swith.utils.error.ErrorType
import com.example.swith.utils.error.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.collections.ArrayList

class RoundViewModel() : BaseViewModel() {
    private val repository = RoundRepository(RoundRemoteDataSource())
    private var allData = ArrayList<GetSessionRes>()
    private var postData = ArrayList<GetSessionRes>()

    var groupIdx = 0
    private var pastVisible = false
    private var curSessionIdx = 0
    private var _roundLiveData = SingleLiveEvent<Round>()

    // 캘린더 화면에 관한 것
    private var _calendarLiveData = MutableLiveData<ArrayList<GetSessionRes>>()

    // Tab 화면
    private var _sessionLiveData = MutableLiveData<SessionInfo>()

    private var _attendLiveEvent = SingleLiveEvent<Any>()

    val roundLiveData : LiveData<Round>
        get() = _roundLiveData

    val calendarLiveData : LiveData<ArrayList<GetSessionRes>>
        get () = _calendarLiveData

    val sessionLiveData: LiveData<SessionInfo>
        get() = _sessionLiveData

    val attendLiveEvent: LiveData<Any>
        get() = _attendLiveEvent

    // private val userIdx = SharedPrefManager().getLoginData()?.userIdx
    private val userIdx = 1
    var curUserAttend: GetAttendance? = null

    fun loadData(){
        viewModelScope.launch {
            val res = repository.getAllRound(this@RoundViewModel, userIdx, groupIdx)
            withContext(Dispatchers.Main) {
                postData.clear()
                allData.clear()
                if (res == null){
                    mutableScreenState.postValue(ScreenState.RENDER)
                }
                res?.let {
                    mutableScreenState.postValue(ScreenState.RENDER)
                    it.getSessionResList.forEach { s ->
                        if (compareWithNow(s)) postData.add(s)
                        allData.add(s)
                    }
                    _roundLiveData.value = it.apply { getSessionResList = postData }
                }
            }
        }
    }

    fun loadAllData(){
        viewModelScope.launch {
            val res = repository.getAllRound(this@RoundViewModel, userIdx, groupIdx)
            withContext(Dispatchers.Main) {
                allData.clear()
                if (res == null){
                    mutableScreenState.postValue(ScreenState.RENDER)
                }
                res?.let {
                    mutableScreenState.postValue(ScreenState.RENDER)
                    it.getSessionResList.forEach { s ->
                        allData.add(s)
                    }
                    _roundLiveData.value = it.apply { getSessionResList = allData }
                }
            }
        }
    }

    fun loadInfoData(){
        viewModelScope.launch {
            val res = repository.getSessionInfo(this@RoundViewModel, userIdx, curSessionIdx)
            if (res == null) mutableScreenState.postValue(ScreenState.RENDER)
            res?.let {
                it.getAttendanceList.forEach { a ->
                    if (a.userIdx == userIdx) curUserAttend = a
                }
                mutableScreenState.postValue(ScreenState.RENDER)
                _sessionLiveData.value = res
            }
        }
    }

    fun getAttendValidTime(): Int = sessionLiveData.value?.attendanceValidTime!!

    fun getCurrentSession(): Int{
        sessionLiveData.value?.let {
            return it.sessionNum
        }
        return 0
    }

    fun setCurrentData(sessionIdx: Int){
        curSessionIdx = sessionIdx
    }

    fun setPastData(pastVisible: Boolean){
        this.pastVisible = pastVisible
        _roundLiveData.value = _roundLiveData.value?.apply { getSessionResList = if(pastVisible) allData else postData }
    }

    // 현재시간과 각 세션 시간 비교
    private fun compareWithNow(session: GetSessionRes) : Boolean{
        with(ZonedDateTime.now(ZoneId.of("Asia/Seoul"))){
            val nowTimeToLong = String.format("%4d%02d%02d%02d%02d", year, monthValue, dayOfMonth, hour, minute).toLong()
            val sessionTimeToLong = String.format("%4d%02d%02d%02d%02d", session.sessionStart[0], session.sessionStart[1], session.sessionStart[2], session.sessionStart[3], session.sessionStart[4]).toLong()
            if (sessionTimeToLong < nowTimeToLong) return false
        }
        return true
    }

     // 해당 날짜에 회차가 있는지 여부 체크
    fun roundDayExists(year: Int, month: Int, day: Int) : Boolean{
        val thisTimeToLong = String.format("%4d%02d%02d", year, month, day).toLong()
        allData.forEach {
            val startTimeToLong = String.format("%4d%02d%02d", it.sessionStart[0], it.sessionStart[1], it.sessionStart[2]).toLong()
            val endTimeToLong = String.format("%4d%02d%02d", it.sessionEnd[0], it.sessionEnd[1], it.sessionEnd[2]).toLong()

            if (thisTimeToLong in startTimeToLong..endTimeToLong) return true
        }
        return false
    }

    // 캘린더에 보여질 data 설정
    fun setCalendarData(year: Int, month: Int, day: Int){
        val tempList = ArrayList<GetSessionRes>()
        val thisTimeToLong = String.format("%4d%02d%02d", year, month, day).toLong()
        allData.forEach {
            val startTimeToLong = String.format("%4d%02d%02d",it.sessionStart[0], it.sessionStart[1], it.sessionStart[2]).toLong()
            val endTimeToLong = String.format("%4d%02d%02d", it.sessionEnd[0], it.sessionEnd[1], it.sessionEnd[2]).toLong()

            if (thisTimeToLong in startTimeToLong..endTimeToLong) tempList.add(it)
        }
        _calendarLiveData.value = tempList
    }

    fun isUpdateAvailable() : Boolean{
        // Todo: 시간 조건 비교하는 것 추가
        if (curUserAttend?.status == 0) return true
        return false
    }

    // 현재 유저 출석 업데이트
    fun updateCurAttend(){
        viewModelScope.launch {
            val res = repository.updateAttend(this@RoundViewModel, userIdx, curSessionIdx)
            withContext(Dispatchers.Main){
                if (res?.isSuccess == false) {
                    mutableErrorMessage.postValue(res.message)
                }
                else{
                    _attendLiveEvent.call()
                }
            }
        }
    }
}