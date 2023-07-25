package com.example.currencyconversions.domain.usecase

import com.example.currencyconversions.data.DataStateWrapper
import com.example.currencyconversions.domain.entity.database.ConversionRatesDay
import com.example.currencyconversions.domain.repo.CurrencyRepo

class CurrencyUseCase(private val currencyRepo: CurrencyRepo) {

    suspend fun getToDayConversionCurrency(): DataStateWrapper<ConversionRatesDay> {
        return currencyRepo.getToDayConversionCurrency() as DataStateWrapper<ConversionRatesDay>
    }

    private suspend fun getDayConversionCurrency(date: String): DataStateWrapper<ConversionRatesDay> {
        return currencyRepo.getDayConversionCurrency(date) as DataStateWrapper<ConversionRatesDay>
    }
}