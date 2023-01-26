package com.example.swith.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.example.swith.domain.entity.ProfileResponse
import com.example.swith.domain.entity.ProfileResult
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileModifyViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _profileData = savedStateHandle.getLiveData<ProfileResult>("profile")
    val profileData : LiveData<ProfileResult> get() = _profileData

    private val _interest1 = MutableLiveData<Int>()
    val interest1 : LiveData<Int> get() = _interest1

    private val _interest2 = MutableLiveData<Int>()
    val interest2 : LiveData<Int> get() = _interest2

    private val _region = MutableLiveData<String>()
    val region : LiveData<String> get() =  _region


    val isLoading: ObservableField<Boolean> = ObservableField<Boolean>()

    val editEnabled : ObservableField<Boolean> = ObservableField<Boolean>()
    init {
        hideLoading()
        // 관심사 idx 설정
        _profileData.value?.let {
            _interest1.value = it.interestIdx1
            _interest2.value = it.interestIdx2
            _region.value = it.region
        }
    }

    fun showLoading() {
        isLoading.set(true)
    }

    fun hideLoading() {
        isLoading.set(false)
    }

    fun setInterest(first : Boolean, interestIdx: Int){
        if (first)
            _interest1.value = interestIdx
        else
            _interest2.value = interestIdx
    }
}