package com.example.swith.repository.profile

import com.example.swith.sourse.ProfileDataSourse

object ProfileRepositoryProvider {
    fun provideProfileRepository() = ProfileRepository(ProfileDataSourse())
}