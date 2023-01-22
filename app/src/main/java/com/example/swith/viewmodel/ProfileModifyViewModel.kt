package com.example.swith.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swith.domain.entity.ProfileResponse


class ProfileModifyViewModel : ViewModel() {
    val currentProfile = MutableLiveData<ProfileResponse>()
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

//    fun requestCurrentProfile(profileRequest: ProfileRequest): LiveData<ProfileResponse> {
//        return mProfileModifyRepository.requestCurrentProfile(profileRequest)
//    }
//
//    fun getCurrentProfile(): LiveData<ProfileResponse> {
//        return mProfileModifyRepository.getCurrentProfile()
//    }
//
//    fun requestCurrentProfileModify(profileModifyRequest: ProfileModifyRequest): LiveData<ProfileModifyResponse> {
//        return mProfileModifyRepository.requestCurrentProfileModify(profileModifyRequest)
//    }
//
//    fun getCurrentProfileModify(): LiveData<ProfileModifyResponse> {
//        return mProfileModifyRepository.getCurrentProfileModify()
//    }
//
//    fun setImage() {
//
//    }

    class Factory : ViewModelProvider.NewInstanceFactory() {

        //@Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileModifyViewModel() as T
        }
    }
}