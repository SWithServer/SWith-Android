package com.example.swith.repository.profile

import com.example.swith.sourse.ProfileDataSourse
import com.example.swith.sourse.ProfileModifyDataSourse

object ProfileModifyRepositoryProvider {
    fun provideProfileModifyRepository() = ProfileModifyRepository(ProfileDataSourse(),ProfileModifyDataSourse())
}