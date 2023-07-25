package com.example.currencyconversions.domain.entity.response

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class DayConversionCurrencyResponse(
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: JsonObject,
    @SerializedName("timestamp")
    val timestamp: Int
) : BaseResponse()