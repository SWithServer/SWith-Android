package com.example.swith.repository.profile

import com.example.swith.remote.ProfileDataSource

object ProfileRepositoryProvider {
    fun provideProfileRepository() = ProfileRepository(ProfileDataSource())
}