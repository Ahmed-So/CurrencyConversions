package com.example.currencyconversions.ui.main.details

import androidx.lifecycle.viewModelScope
import com.example.currencyconversions.data.DataStateWrapper
import com.example.currencyconversions.domain.entity.CurrencyRatio
import com.example.currencyconversions.domain.entity.database.ConversionRatesDay
import com.example.currencyconversions.domain.usecase.CurrencyUseCase
import com.example.currencyconversions.ui.base.BaseViewModel
import com.example.currencyconversions.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val currencyUseCase: CurrencyUseCase) :
    BaseViewModel(
    ) {

    lateinit var fromCurrency: String
    lateinit var toCurrency: String
    private var conversionToDay: ConversionRatesDay? = null

    private val _topTenCurrencies = MutableStateFlow<ArrayList<CurrencyRatio>?>(null)
    val topTenCurrencies: MutableStateFlow<ArrayList<CurrencyRatio>?> = _topTenCurrencies
    private val _historicalConversions = MutableStateFlow<ArrayList<CurrencyRatio>?>(null)
    val historicalConversions: MutableStateFlow<ArrayList<CurrencyRatio>?> = _historicalConversions

    fun fetchTopTenCurrencies() {

        if (!this::fromCurrency.isInitialized || (_topTenCurrencies.value?.size ?: 0) > 0)
            return

        if (conversionToDay == null)
            viewModelScope.launch {
                when (val conversions = currencyUseCase.getToDayConversionCurrency()) {
                    is DataStateWrapper.Success -> conversionToDay = conversions.data
                    is DataStateWrapper.Error -> {
                        _error.value = conversions.message
                        return@launch
                    }
                }
            }

        val topTen = currencyUseCase.getTopTenCurrencies()
        val result = ArrayList<CurrencyRatio>()
        val date = Utils.dateFormat(Date(conversionToDay!!.date))

        topTen.forEach {
            val rate = currencyUseCase.convertCurrency(conversionToDay!!, fromCurrency, it, 1.0)
            if (rate is DataStateWrapper.Success)
                result.add(CurrencyRatio(date, fromCurrency, it, rate.data))
        }
        _topTenCurrencies.value = result
    }

    fun fetchHistoricalData() {
        if ((_historicalConversions.value?.size ?: 0) > 0)
            return

        _loading.value = true
        viewModelScope.launch {
            when (val result = currencyUseCase.getHistoricalData(fromCurrency, toCurrency)) {
                is DataStateWrapper.Success -> _historicalConversions.value = result.data
                is DataStateWrapper.Error -> _error.value = result.message
            }
            _loading.value = false
        }
    }
}