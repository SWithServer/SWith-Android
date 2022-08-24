package com.example.swith.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swith.data.ProfileRequest
import com.example.swith.data.ProfileResponse
import com.example.swith.data.ResumeResponse
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

    fun requestCurrentResume(userIdx:Long): LiveData<ResumeResponse> {
        return mResumeRepository.requestCurrentResume(userIdx)
    }

    fun getCurrentResume(): LiveData<ResumeResponse> {
        return mResumeRepository.getCurrentResume()
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {

        //@Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ResumeViewModel() as T
        }
    }
}