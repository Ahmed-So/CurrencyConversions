package com.example.currencyconversions.data.repo

import com.example.currencyconversions.data.DataStateWrapper
import com.example.currencyconversions.domain.repo.CurrencyRepo
import com.example.currencyconversions.data.database.AppDatabaseDao
import com.example.currencyconversions.data.remote.APIService
import com.example.currencyconversions.data.remote.apiCallHandler
import com.example.currencyconversions.domain.entity.database.ConversionRatesDay
import com.example.currencyconversions.utils.Utils
import java.util.Calendar

class CurrencyRepoImpl(
    private val apiService: APIService,
    private val appDatabaseDao: AppDatabaseDao
) : CurrencyRepo {

    private var conversionToDay: ConversionRatesDay? = null

    override suspend fun getToDayConversionCurrency(): DataStateWrapper<ConversionRatesDay?> {
        if (conversionToDay != null)
            return DataStateWrapper.Success(conversionToDay!!)

        val result = getDayConversionCurrency(Utils.dateFormat(Calendar.getInstance().time))
        if (result is DataStateWrapper.Success)
            conversionToDay = result.data
        return result
    }

    override suspend fun getDayConversionCurrency(date: String): DataStateWrapper<ConversionRatesDay?> {
        var conversionDay = appDatabaseDao.getConversionRate(Utils.dateFormat(date).time)

        if (conversionDay != null)
            return DataStateWrapper.Success(conversionDay)

        val response = apiCallHandler { apiService.getDayConversionCurrency(date) }
        return if (response is DataStateWrapper.Success) {
            conversionDay = ConversionRatesDay.mapper(response.data)
            appDatabaseDao.insert(conversionDay)

            DataStateWrapper.Success(conversionDay)
        } else {
            val errorResponse: DataStateWrapper.Error = response as DataStateWrapper.Error
            DataStateWrapper.Error(errorResponse.code, errorResponse.message)
        }
    }

    override suspend fun getAllCurrencies(): DataStateWrapper<ArrayList<String>> {

        val response = getToDayConversionCurrency()

        return if (response is DataStateWrapper.Success) {
            val result: ArrayList<String> = ArrayList()
            result.addAll(response.data!!.getRates().keySet().toList())
            DataStateWrapper.Success(result)
        } else {
            val errorResponse: DataStateWrapper.Error = response as DataStateWrapper.Error
            DataStateWrapper.Error(errorResponse.code, errorResponse.message)
        }
    }

    override fun getTopTenCurrencies(): Array<String> {
        return arrayOf("USD", "EUR", "GBP", "JPY", "CHF", "CNY", "EGP", "AED", "SAR", "KWD")
    }

    override suspend fun getHistoricalData(): DataStateWrapper<ArrayList<ConversionRatesDay>> {
        val result = ArrayList<ConversionRatesDay>()
        val calendar = Calendar.getInstance()
        var conversions: DataStateWrapper<ConversionRatesDay?>? = null
        for (count in 0..4) {
            calendar.add(Calendar.DATE, -1)
            conversions = getDayConversionCurrency(Utils.dateFormat(calendar.time))
            if (conversions is DataStateWrapper.Success)
                conversions.data?.let { result.add(it) }
        }

        return if (result.size > 0)
            DataStateWrapper.Success(result)
        else
            conversions as DataStateWrapper.Error
    }
}