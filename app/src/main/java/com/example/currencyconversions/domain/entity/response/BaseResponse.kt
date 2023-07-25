package com.example.currencyconversions.domain.entity.response

import com.google.gson.annotations.SerializedName

abstract class BaseResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("error")
    var error: Error? = null
)

class Error(
    @SerializedName("code")
    val code: Int,
    @SerializedName("info")
    val info: String,
    @SerializedName("type")
    val type: String
)