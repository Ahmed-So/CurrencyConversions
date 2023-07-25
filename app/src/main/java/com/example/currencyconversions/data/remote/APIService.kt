package com.example.currencyconversions.data.remote

import com.example.currencyconversions.domain.entity.response.DayConversionCurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {

    @GET("{date}")
    suspend fun getDayConversionCurrency(@Path("date") date: String): DayConversionCurrencyResponse
}