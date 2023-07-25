package com.example.currencyconversions.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

object Utils {

    const val SERVER_DATE_FORMAT = "yyyy-MM-dd"

    fun dateFormat(date: String): Date {
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat(SERVER_DATE_FORMAT)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
        return simpleDateFormat.parse(date)!!
    }

    fun dateFormat(date: Date): String {
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat(SERVER_DATE_FORMAT)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
        return simpleDateFormat.format(date)
    }
}