package com.example.swith.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swith.data.DateTime
import com.example.swith.data.Group
import com.example.swith.data.GroupItem
import com.example.swith.repository.home.HomeRepository
import com.example.swith.utils.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel() : ViewModel() {
    // 임시 변수
    private var studyCount = 0

    private var groupData = ArrayList<GroupItem>()
    private var _groupLiveData = MutableLiveData<Group>()
    val groupLiveData: LiveData<Group>
        get() = _groupLiveData

    init{
        // val userId = SharedPrefManager().getLoginData()?.userIdx
        val userId = 1
        viewModelScope.launch{
            val temp = HomeRepository().getAllStudy(userId)
            Log.e("viewModel test", temp.toString())
            // 현재 오류로 null 값 반환됨
            withContext(Dispatchers.Main){
                groupData.apply {
                    add(GroupItem(1, "알고리즘 스터디", 8, "컴퓨터", "그룹 1 입니다 21", 3, "알고리즘 학습3",
                        listOf(2022, 7, 28, 12, 0), 50 ))
                    add(GroupItem(2, "토익 스터디", 10, "어학", "그룹 2 입니다 4", 5, "알고리즘 학습5",
                        listOf(2022, 7, 28, 16, 0), 50))
                }
                _groupLiveData.value = Group(groupData)
                // _groupLiveData.value = temp!!
            }
        }


    }
}