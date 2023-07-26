package com.example.currencyconversions.domain.entity

data class CurrencyRatio(
    val date: String,
    val fromCurrency: String,
    val toCurrency: String,
    val ratio: String
) {
}