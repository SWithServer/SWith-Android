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
    val profileData : LiveData<ProfileResult>
        get() = _profileData

    val isLoading: ObservableField<Boolean> = ObservableField<Boolean>()

    init {
        hideLoading()
    }

    fun showLoading() {
        isLoading.set(true)
    }

    fun hideLoading() {
        isLoading.set(false)
    }

}