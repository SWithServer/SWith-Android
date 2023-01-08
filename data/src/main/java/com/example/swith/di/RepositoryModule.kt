package com.example.swith.di

import com.example.swith.repository.AnnounceRepository
import com.example.swith.repository.announce.AnnounceRepositoryImpl
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
}