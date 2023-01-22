package com.example.swith.viewmodel

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
    val isLoading: ObservableField<Boolean> = ObservableField<Boolean>()

    private val userIdx = sharedPrefManager.getLoginData()?.userIdx ?: 0

    private val _profileData = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileData: StateFlow<ProfileState>
        get() = _profileData


    fun getProfileData() {
        viewModelScope.launch {
            getCurrentProfileUseCase(userIdx)
                .catch {
                    _profileData.value = ProfileState.Error
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