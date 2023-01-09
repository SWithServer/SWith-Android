package com.example.swith.viewmodel

import androidx.lifecycle.ViewModel
import com.example.swith.entity.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(

) : ViewModel(){
    private var _noticeList = MutableStateFlow<MutableList<Notification>>(mutableListOf())
    val noticeList : StateFlow<List<Notification>>
        get() = _noticeList

    init {
        // Todo : 임시 데이터
        _noticeList.value = mutableListOf(
            Notification("'부천 인천 중급 영어 스터디' 스터디가 종료되었습니다!", "22/08/07"),
            Notification("3회차는 어쩍를 공부할 예정입니다. 미리 챕터 5까지 읽어와 주시고 발표 준비바랍니다.","22/09/07"),
            Notification("4회차는 어쩍를 공부할 예정입니다. 미리 챕터 6까지 읽어와 주시고 발표 준비바랍니다.","22/09/14"),
            Notification("5회차는 어쩍를 공부할 예정입니다. 미리 챕터 7까지 읽어와 주시고 발표 준비바랍니다.","22/09/21")
        )
    }
}