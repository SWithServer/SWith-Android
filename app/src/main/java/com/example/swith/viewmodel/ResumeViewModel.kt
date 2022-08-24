package com.example.swith.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swith.data.ProfileRequest
import com.example.swith.data.ProfileResponse
import com.example.swith.repository.resume.ResumeRepository
import com.example.swith.repository.resume.ResumeRepositoryProvider

class ResumeViewModel:ViewModel() {
    val isLoading: ObservableField<Boolean> = ObservableField<Boolean>()
    private val mResumeRepository: ResumeRepository = ResumeRepositoryProvider.resumeRepository()
    init {
        hideLoading()
    }

    fun showLoading() {
        isLoading.set(true)
    }

    fun hideLoading() {
        isLoading.set(false)
    }

//    fun requestCurrentResume(profileRequest: ProfileRequest): LiveData<> {
//        return mResumeRepository.requestCurrentResume(profileRequest)
//    }
//
//    fun getCurrentResume(): LiveData<> {
//        return mResumeRepository.getCurrentResume()
//    }

    class Factory : ViewModelProvider.NewInstanceFactory() {

        //@Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ResumeViewModel() as T
        }
    }
}