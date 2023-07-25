package com.example.currencyconversions.data

sealed class DataStateWrapper<out T> {
    data class Success<out T>(val data: T) : DataStateWrapper<T>()
    data class Error(val code: Int? = null, val message: String? = null) :
        DataStateWrapper<Nothing>()
}