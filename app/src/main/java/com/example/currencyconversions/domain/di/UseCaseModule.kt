package com.example.currencyconversions.domain.di

import com.example.currencyconversions.domain.repo.CurrencyRepo
import com.example.currencyconversions.domain.usecase.CurrencyUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideUseCase(currencyRepo: CurrencyRepo): CurrencyUseCase {
        return CurrencyUseCase(currencyRepo)
    }
}