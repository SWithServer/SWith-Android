package com.example.swith.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swith.R
import com.example.swith.domain.entity.Group
import com.example.swith.domain.usecase.home.GetHomeDataUseCase
import com.example.swith.utils.SharedPrefManager
import com.example.swith.utils.UiText
import com.example.swith.utils.base.BaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val sharedPrefManager: SharedPrefManager
) : ViewModel() {
    private var _groupData = MutableStateFlow<BaseState<List<Group>>>(BaseState.Loading)

    val groupData: StateFlow<BaseState<List<Group>>>
        get() = _groupData

    private val _errorChannel = Channel<UiText>()
    val errors = _errorChannel.receiveAsFlow()

    val isLoading : ObservableField<Boolean> = ObservableField(true)
    val empty : ObservableField<Boolean> = ObservableField(false)

    private val userIdx = sharedPrefManager.getLoginData()?.userIdx ?: 0
    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            getHomeDataUseCase(userIdx)
                .catch {
                    _groupData.value = BaseState.Error(it.message)
                    empty.set(true)
                    isLoading.set(false)
                    it.message?.let { t -> _errorChannel.send(UiText.StringResource(R.string.home_error)) }
                }.collectLatest {
                    _groupData.value = BaseState.Success(it)
                    empty.set(it.isNotEmpty())
                    isLoading.set(false)
                }
        }
    }

}