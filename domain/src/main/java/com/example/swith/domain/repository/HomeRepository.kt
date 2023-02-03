package com.example.swith.domain.repository

import com.example.swith.domain.entity.Group
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun loadHomeData(userIdx: Long) : Flow<List<Group>>
}