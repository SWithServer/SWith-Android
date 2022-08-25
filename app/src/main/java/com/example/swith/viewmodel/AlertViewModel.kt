//package com.example.swith.viewmodel
//
//import androidx.databinding.ObservableField
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.swith.data.LoginRequest
//import com.example.swith.data.LoginResponse
//import com.example.swith.repository.announce.AlertRepository
//import com.example.swith.repository.announce.AlertRepositoryProvider
//
//class AlertViewModel:ViewModel() {
//    val isLoading: ObservableField<Boolean> = ObservableField<Boolean>()
//    private val mAlertRepository: AlertRepository = AlertRepositoryProvider.provideAlertRepository()
//    init {
//        hideLoading()
//    }
//
//    fun showLoading() {
//        isLoading.set(true)
//    }
//
//    fun hideLoading() {
//        isLoading.set(false)
//    }
//
//    fun requestCurrentAlert(loginRequest: LoginRequest): LiveData<LoginResponse> {
//        return mAlertRepository.requestCurrentLogin(loginRequest)
//    }
//
//    fun getCurrentAlert(): LiveData<LoginResponse> {
//        return mAlertRepository.getCurrentLogin()
//    }
//
//    class Factory : ViewModelProvider.NewInstanceFactory() {
//
//        //@Suppress("UNCHECKED_CAST")
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            return AlertViewModel() as T
//        }
//    }
//}