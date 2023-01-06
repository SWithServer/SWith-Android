package com.example.swith.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swith.entity.ProfileResponse
import com.example.swith.repository.profile.ProfileModifyRepository
import com.example.swith.repository.profile.ProfileModifyRepositoryProvider

class ProfileModifyViewModel : ViewModel() {
    val isLoading: ObservableField<Boolean> = ObservableField<Boolean>()
    private val mProfileModifyRepository: ProfileModifyRepository =
        ProfileModifyRepositoryProvider.provideProfileModifyRepository()

    init {
        hideLoading()
    }

    fun showLoading() {
        isLoading.set(true)
    }

    fun hideLoading() {
        isLoading.set(false)
    }

    fun requestCurrentProfile(profileRequest: com.example.swith.entity.ProfileRequest): LiveData<ProfileResponse> {
        return mProfileModifyRepository.requestCurrentProfile(profileRequest)
    }

    fun getCurrentProfile(): LiveData<ProfileResponse> {
        return mProfileModifyRepository.getCurrentProfile()
    }

    fun requestCurrentProfileModify(profileModifyRequest: com.example.swith.entity.ProfileModifyRequest): LiveData<com.example.swith.entity.ProfileModifyResponse> {
        return mProfileModifyRepository.requestCurrentProfileModify(profileModifyRequest)
    }

    fun getCurrentProfileModify(): LiveData<com.example.swith.entity.ProfileModifyResponse> {
        return mProfileModifyRepository.getCurrentProfileModify()
    }

    fun setImage() {

    }

    class Factory : ViewModelProvider.NewInstanceFactory() {

        //@Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileModifyViewModel() as T
        }
    }
}