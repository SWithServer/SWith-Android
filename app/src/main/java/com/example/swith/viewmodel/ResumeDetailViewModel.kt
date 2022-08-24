package com.example.swith.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swith.repository.resume.ResumeDetailRepository
import com.example.swith.repository.resume.ResumeDetailRepositoryProvider

class ResumeDetailViewModel:ViewModel() {
    val isLoading: ObservableField<Boolean> = ObservableField<Boolean>()
    private val mResumeDetailRepository: ResumeDetailRepository = ResumeDetailRepositoryProvider.resumeDetailRepository()
    init {
        hideLoading()
    }

    fun showLoading() {
        isLoading.set(true)
    }

    fun hideLoading() {
        isLoading.set(false)
    }

//    fun requestCurrentResumeDetail(profileRequest: ProfileRequest): LiveData<> {
//        return mResumeDetailRepository.requestCurrentResume(profileRequest)
//    }
//
//    fun getCurrentResumeDetail(): LiveData<> {
//        return mResumeDetailRepository.getCurrentResume()
//    }

    class Factory : ViewModelProvider.NewInstanceFactory() {

        //@Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ResumeDetailViewModel() as T
        }
    }
}