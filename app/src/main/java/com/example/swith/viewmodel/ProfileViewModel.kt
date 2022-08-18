package com.example.swith.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swith.data.ProfileResponse
import com.example.swith.repository.profile.ProfileRepository
import com.example.swith.repository.profile.ProfileRepositoryProvider

class ProfileViewModel: ViewModel(){
    val isLoading: ObservableField<Boolean> = ObservableField<Boolean>()
    private val mProfileRepository: ProfileRepository = ProfileRepositoryProvider.provideProfileRepository()
    init {
        hideLoading()
    }

    fun showLoading() {
        isLoading.set(true)
    }

    fun hideLoading() {
        isLoading.set(false)
    }

    fun requestCurrentProfile(email: String,jwt: String): LiveData<ProfileResponse> {
        return mProfileRepository.requestCurrentProfile(email,jwt)
    }

    fun getCurrentProfile(): LiveData<ProfileResponse> {
        return mProfileRepository.getCurrentProfile()
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {

        //@Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel() as T
        }
    }
}