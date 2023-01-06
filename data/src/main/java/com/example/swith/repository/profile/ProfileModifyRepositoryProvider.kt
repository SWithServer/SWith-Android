package com.example.swith.repository.profile

import com.example.swith.remote.ProfileDataSource
import com.example.swith.remote.ProfileModifyDataSource

object ProfileModifyRepositoryProvider {
    fun provideProfileModifyRepository() = ProfileModifyRepository(
        ProfileDataSource(),
        ProfileModifyDataSource()
    )
}