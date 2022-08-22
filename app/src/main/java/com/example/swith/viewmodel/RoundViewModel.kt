package com.example.swith.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.swith.data.*
import com.example.swith.repository.round.RoundRemoteDataSource
import com.example.swith.repository.round.RoundRepository
import com.example.swith.utils.SingleLiveEvent
import com.example.swith.utils.base.BaseViewModel
import com.example.swith.utils.compareTimeWithNow
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
    var pastVisible = false
    private var curSessionIdx = 0
    private var _roundLiveData = SingleLiveEvent<Round>()

    // 캘린더 화면에 관한 것
    private var _calendarLiveData = MutableLiveData<ArrayList<GetSessionRes>>()

    // Tab 화면
    private var _sessionLiveData = MutableLiveData<SessionInfo>()

    // 출석
    private var _attendLiveEvent = SingleLiveEvent<Any>()

    // 통계 화면 (유저별 출석율)
    private var _userAttendLiveData = MutableLiveData<UserAttend>()

    // 메모 화면
    private var _memoLiveEvent = SingleLiveEvent<Any>()

    val roundLiveData : LiveData<Round>
        get() = _roundLiveData

    val calendarLiveData : LiveData<ArrayList<GetSessionRes>>
        get () = _calendarLiveData

    val sessionLiveData: LiveData<SessionInfo>
        get() = _sessionLiveData

    val attendLiveEvent: LiveData<Any>
        get() = _attendLiveEvent

    val userAttendLiveData : LiveData<UserAttend>
        get() = _userAttendLiveData

    val memoLiveEvent: LiveData<Any>
        get() = _memoLiveEvent

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
                        if (compareTimeWithNow(s.sessionEnd)) postData.add(s)
                        allData.add(s)
                    }
                    allData.sortBy { s -> s.sessionNum }
                    postData.sortBy { s -> s.sessionNum }
                    _roundLiveData.value = it.apply { getSessionResList = if(pastVisible) allData else postData }
                }
            }
        }
    }

    fun loadInfoData(){
        viewModelScope.launch {
            val res = repository.getSessionInfo(this@RoundViewModel, userIdx, curSessionIdx)
            if (res == null) mutableScreenState.postValue(ScreenState.RENDER)
            res?.let {
                curUserAttend = null
                it.getAttendanceList?.forEach { a ->
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

    fun setPastData(){
        _roundLiveData.value = _roundLiveData.value?.apply { getSessionResList = if(pastVisible) allData else postData }
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
        mutableScreenState.postValue(ScreenState.LOAD)
    }

    fun isUpdateAvailable() : Boolean{
        if (curUserAttend?.status == 0)
            _sessionLiveData.value?.let { return compareTimeWithNow(it.sessionEnd) }
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

    fun loadUserAttend(){
        viewModelScope.launch {
            val res = repository.getUserAttend(this@RoundViewModel, groupIdx)
            withContext(Dispatchers.Main){
                if (res == null) mutableScreenState.postValue(ScreenState.RENDER)
                res?.let {
                    mutableScreenState.postValue(ScreenState.RENDER)
                    _userAttendLiveData.value = res
                }
            }
        }
    }

    fun createMemo(content: String){
        viewModelScope.launch {
            val res = repository.createMemo(this@RoundViewModel, Memo(content, curSessionIdx, userIdx))
            withContext(Dispatchers.Main){
                if (res?.isSuccess == false)
                    mutableErrorMessage.postValue(res.message)
                else _memoLiveEvent.call()
            }
        }
    }

    fun updateMemo(content: String){
        viewModelScope.launch {
            val res = repository.updateMemo(this@RoundViewModel, MemoUpdate(content, sessionLiveData.value?.memoIdx!!))
            withContext(Dispatchers.Main){
                if (res?.isSuccess == false)
                    mutableErrorMessage.postValue(res.message)
                else _memoLiveEvent.call()
            }
        }
    }
}