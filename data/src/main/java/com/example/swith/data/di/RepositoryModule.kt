package com.example.swith.data.di

import com.example.swith.data.repository.announce.AnnounceRepositoryImpl
import com.example.swith.data.repository.profile.ProfileRepositoryImpl
import com.example.swith.domain.repository.AnnounceRepository
import com.example.swith.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAnnounceRepository(announceRepositoryImpl: AnnounceRepositoryImpl) : AnnounceRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl) : ProfileRepository
}