package com.example.currencyconversions.ui.main.convertCurrency

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyconversions.data.DataStateWrapper
import com.example.currencyconversions.domain.entity.database.ConversionRatesDay
import com.example.currencyconversions.domain.usecase.CurrencyUseCase
import com.example.currencyconversions.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConvertCurrencyViewModel @Inject constructor(private val currencyUseCase: CurrencyUseCase) :
    BaseViewModel() {

    companion object {
        private const val AMOUNT_DEFAULT_VALUE: String = "1"
    }

    private var conversionToDay: ConversionRatesDay? = null

    private val _allCurrencies: MutableStateFlow<ArrayList<String>?> = MutableStateFlow(null)
    val allCurrencies: StateFlow<List<String>?> = _allCurrencies

    val fromAmount: MutableLiveData<String> = MutableLiveData(AMOUNT_DEFAULT_VALUE)
    val toAmount: MutableLiveData<String> = MutableLiveData(AMOUNT_DEFAULT_VALUE)

    fun fetchAllCurrencies() {
        if (_allCurrencies.value != null)
            return
        _loading.value = true
        viewModelScope.launch {
            when (val result = currencyUseCase.getAllCurrencies()) {
                is DataStateWrapper.Success -> _allCurrencies.value = result.data
                is DataStateWrapper.Error -> _error.value = result.message
            }
            _loading.value = false
        }
    }

    fun convert(isFrom: Boolean, fromCurrency: String, toCurrency: String) {
        val amountSource = if (isFrom) fromAmount else toAmount
        if (amountSource.value.isNullOrBlank()) amountSource.value = AMOUNT_DEFAULT_VALUE
        if (fromCurrency == toCurrency) {
            updateConvertedAmount(isFrom, amountSource.value.toString())
            return
        }

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

        when (val result = currencyUseCase.convertCurrency(
            conversionToDay!!,
            fromCurrency,
            toCurrency,
            amountSource.value!!.toDouble()
        )) {
            is DataStateWrapper.Success -> updateConvertedAmount(isFrom, result.data)
            is DataStateWrapper.Error -> _error.value = result.message
        }
    }

    private fun updateConvertedAmount(isFrom: Boolean, amount: String) {
        if (isFrom)
            toAmount.value = amount
        else
            fromAmount.value = amount
    }
}