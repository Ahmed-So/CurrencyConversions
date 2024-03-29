package com.example.currencyconversions.domain.repo

import com.example.currencyconversions.data.DataStateWrapper
import com.example.currencyconversions.domain.entity.database.ConversionRatesDay

interface CurrencyRepo {

    suspend fun getToDayConversionCurrency(): DataStateWrapper<ConversionRatesDay?>

    suspend fun getDayConversionCurrency(date: String): DataStateWrapper<ConversionRatesDay?>

    suspend fun getAllCurrencies(): DataStateWrapper<ArrayList<String>>

    fun getTopTenCurrencies(): Array<String>

    suspend fun getHistoricalData(): DataStateWrapper<ArrayList<ConversionRatesDay>>
}