package com.amineaytac.lunessa.core.data.di

import com.amineaytac.lunessa.core.data.repo.MakeupRepository
import com.amineaytac.lunessa.core.data.repo.MakeupRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMakeupRepository(makeupRepositoryImpl: MakeupRepositoryImpl): MakeupRepository
}