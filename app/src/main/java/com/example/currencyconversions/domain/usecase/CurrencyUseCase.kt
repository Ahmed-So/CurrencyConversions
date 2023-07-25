package com.example.currencyconversions.domain.usecase

import com.example.currencyconversions.data.DataStateWrapper
import com.example.currencyconversions.domain.entity.database.ConversionRatesDay
import com.example.currencyconversions.domain.repo.CurrencyRepo
import com.google.gson.JsonObject

class CurrencyUseCase(private val currencyRepo: CurrencyRepo) {

    suspend fun getToDayConversionCurrency(): DataStateWrapper<ConversionRatesDay> {
        return currencyRepo.getToDayConversionCurrency() as DataStateWrapper<ConversionRatesDay>
    }

    private suspend fun getDayConversionCurrency(date: String): DataStateWrapper<ConversionRatesDay> {
        return currencyRepo.getDayConversionCurrency(date) as DataStateWrapper<ConversionRatesDay>
    }

    suspend fun getAllCurrencies(): DataStateWrapper<ArrayList<String>> {
        return currencyRepo.getAllCurrencies()
    }

    fun convertCurrency(
        conversionRatesDay: ConversionRatesDay,
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): DataStateWrapper<String> {
        val percentage =
            getCurrencyPercentage(fromCurrency, toCurrency, conversionRatesDay.getRates())
        return DataStateWrapper.Success(String.format("%.2f", percentage * amount))
    }

    private fun getCurrencyPercentage(from: String, to: String, rates: JsonObject): Double {
        return when ("EUR") {
            from -> rates.get(to).asDouble
            to -> 1 / rates.get(from).asDouble
            else -> (1 / rates.get(from).asDouble) * rates.get(to).asDouble
        }
    }
}