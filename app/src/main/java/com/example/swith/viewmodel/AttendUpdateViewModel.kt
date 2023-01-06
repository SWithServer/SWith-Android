package com.example.swith.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.swith.repository.manage.attend.ManageAttendRemoteDataSource
import com.example.swith.repository.manage.attend.ManageAttendRepository
import com.example.swith.utils.SingleLiveEvent
import com.example.swith.utils.base.BaseViewModel
import com.example.swith.utils.error.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AttendUpdateViewModel : BaseViewModel() {
    private val repository = ManageAttendRepository(ManageAttendRemoteDataSource())

    private var _attendLiveData = MutableLiveData<com.example.swith.entity.AttendList>()
    private val _updateAttendLiveEvent = SingleLiveEvent<Any>()

    val attendLiveData: LiveData<com.example.swith.entity.AttendList>
        get() = _attendLiveData

    val updateAttendLiveEvent: LiveData<Any>
        get() = _updateAttendLiveEvent

    fun loadData(groupIdx: Long) {
        viewModelScope.launch {
            val res = repository.getAttendData(this@AttendUpdateViewModel, groupIdx)
            withContext(Dispatchers.Main) {
                if (res == null) mutableScreenState.postValue(ScreenState.RENDER)
                res?.let {
                    mutableScreenState.postValue(ScreenState.RENDER)
                    res.attend.sortBy { a -> a.sessionNum }
                    _attendLiveData.value = res
                }
            }
        }
    }

    fun updateData(attendList: List<com.example.swith.entity.UpdateAttend>) {
        viewModelScope.launch {
            mutableScreenState.postValue(ScreenState.LOAD)
            val res = repository.updateAttendData(this@AttendUpdateViewModel, attendList)
            withContext(Dispatchers.Main) {
                res?.let {
                    _updateAttendLiveEvent.call()
                }
                mutableScreenState.postValue(ScreenState.RENDER)
            }
        }
    }
}