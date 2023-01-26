package com.example.swith.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.usecase.profile.GetCurrentProfileUseCase
import com.example.swith.domain.entity.ProfileResult
import com.example.swith.utils.SharedPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentProfileUseCase: GetCurrentProfileUseCase,
    private val sharedPrefManager: SharedPrefManager,
) : ViewModel() {
    private val userIdx = sharedPrefManager.getLoginData()?.userIdx ?: 0

    private val _profileData = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileData: StateFlow<ProfileState>
        get() = _profileData

    init {
        getProfileData()
    }


    private fun getProfileData() {
        viewModelScope.launch {
            getCurrentProfileUseCase(userIdx)
                .catch {
                    Log.e("error", it.message.toString())
                    _profileData.value = ProfileState.Error
//                    _profileData.value = ProfileState.Success(ProfileResult(
//                        "token", 1L, "email", 1, 0, "서울특별시 동작구",
//                        "자기소개", "동건동건", null, "token", "역할", 1, 1))
                }
                .collectLatest {
                    _profileData.value = ProfileState.Success(it)
                }
        }
    }
//    fun showLoading() {
//        isLoading.set(true)
//    }
//
//    fun hideLoading() {
//        isLoading.set(false)
//    }

//    fun requestCurrentProfile(profileRequest: com.example.swith.entity.ProfileRequest): LiveData<ProfileResponse> {
//        return mProfileRepository.requestCurrentProfile(profileRequest)
//    }
//
//    fun getCurrentProfile(): LiveData<ProfileResponse> {
//        return mProfileRepository.getCurrentProfile()
//    }
}

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val profile: ProfileResult) : ProfileState()
    object Error : ProfileState()
}