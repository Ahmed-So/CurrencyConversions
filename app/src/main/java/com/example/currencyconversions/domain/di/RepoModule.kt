package com.example.currencyconversions.domain.di

import com.example.currencyconversions.data.database.AppDatabaseDao
import com.example.currencyconversions.data.remote.APIService
import com.example.currencyconversions.data.repo.CurrencyRepoImpl
import com.example.currencyconversions.domain.repo.CurrencyRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    fun provideRepo(apiService: APIService, databaseDao: AppDatabaseDao): CurrencyRepo {
        return CurrencyRepoImpl(apiService, databaseDao)
    }
}