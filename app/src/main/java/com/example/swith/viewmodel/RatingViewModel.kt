package com.example.swith.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.swith.entity.RatingResponse
import com.example.swith.repository.rating.RatingRepository
import com.example.swith.repository.rating.RatingRepositoryProvider

class RatingViewModel : ViewModel() {
    val isLoading: ObservableField<Boolean> = ObservableField<Boolean>()
    private val mRatingRepository: RatingRepository =
        RatingRepositoryProvider.provideRatingRepository()

    init {
        hideLoading()
    }

    fun showLoading() {
        isLoading.set(true)
    }

    fun hideLoading() {
        isLoading.set(false)
    }

    //    fun requestCurrentRating(loginRequest: LoginRequest): LiveData<LoginResponse> {
//        return mRatingRepository
//    }
//
//    fun getCurrentRating(): LiveData<LoginResponse> {
//        return mRatingRepository
//    }
    fun requestCurrentUserList(
        groupIdx: String,
        userIdx: com.example.swith.entity.ProfileRequest
    ): LiveData<RatingResponse> {
        return mRatingRepository.requestCurrentUserList(groupIdx, userIdx)
    }

    fun getCurrentUserList(): LiveData<RatingResponse> {
        return mRatingRepository.getCurrentUserList()
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {

        //@Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RatingViewModel() as T
        }
    }
}