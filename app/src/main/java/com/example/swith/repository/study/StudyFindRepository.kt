package com.example.swith.repository.study

import com.example.swith.data.Group
import com.example.swith.data.StudyGroup
import com.example.swith.utils.error.RemoteErrorEmitter

class StudyFindRepository(private val StudyFindRemoteDataSource : StudyFindRemoteDataSource) {
    //suspend fun getSearchStudy(remoteErrorEmitter: RemoteErrorEmitter) : StudyGroup? = StudyFindRemoteDataSource.getSearchStudy(remoteErrorEmitter)
}