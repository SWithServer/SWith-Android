package com.example.swith.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.swith.entity.AnnounceCreate
import com.example.swith.entity.AnnounceList
import com.example.swith.entity.AnnounceModify
import com.example.swith.remote.announce.AnnounceRemoteDataSource
import com.example.swith.usecase.announce.CreateAnnounceUseCase
import com.example.swith.usecase.announce.DeleteAnnounceUseCase
import com.example.swith.usecase.announce.GetAnnounceDataUseCase
import com.example.swith.usecase.announce.UpdateAnnounceUseCase
import com.example.swith.utils.SingleLiveEvent
import com.example.swith.utils.base.BaseViewModel
import com.example.swith.utils.error.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnounceViewModel @Inject constructor(
    private val getAnnounceDataUseCase: GetAnnounceDataUseCase,
    private val deleteAnnounceUseCase: DeleteAnnounceUseCase,
    private val updateAnnounceUseCase: UpdateAnnounceUseCase,
    private val createAnnounceUseCase: CreateAnnounceUseCase
): BaseViewModel() {
    private var _announceLiveData = SingleLiveEvent<AnnounceList>()
    private var _deleteLiveEvent = SingleLiveEvent<Any>()
    private var _createLiveEvent = SingleLiveEvent<Any>()
    private var _updateLiveEvent = SingleLiveEvent<Any>()

    val announceLiveData: LiveData<AnnounceList>
        get() = _announceLiveData

    val deleteLiveEvent: LiveData<Any>
        get() = _deleteLiveEvent

    val createLiveEvent: LiveData<Any>
        get() = _createLiveEvent

    val updateLiveEvent: LiveData<Any>
        get() = _updateLiveEvent

    fun loadData(groupIdx: Long) {
        viewModelScope.launch {
            getAnnounceDataUseCase(this@AnnounceViewModel, groupIdx)?.let {
                mutableScreenState.postValue(ScreenState.RENDER)
                _announceLiveData.value = it
            }?: run {
                mutableScreenState.postValue(ScreenState.RENDER)
            }
        }
    }

    fun deleteAnnounce(announceIdx: Long) {
        viewModelScope.launch {
            deleteAnnounceUseCase(this@AnnounceViewModel, announceIdx)?.let {
                _deleteLiveEvent.call()
                mutableScreenState.postValue(ScreenState.LOAD)
            }
        }
    }

    fun createAnnounce(announceCreate: AnnounceCreate) {
        viewModelScope.launch {
            createAnnounceUseCase(this@AnnounceViewModel, announceCreate)?.let {
                _createLiveEvent.call()
                mutableScreenState.postValue(ScreenState.LOAD)
            }
        }
    }

    fun updateAnnounce(announceModify: AnnounceModify) {
        viewModelScope.launch {
            updateAnnounceUseCase(this@AnnounceViewModel, announceModify)?.let {
                _updateLiveEvent.call()
                mutableScreenState.postValue(ScreenState.LOAD)
            }
        }
    }

}