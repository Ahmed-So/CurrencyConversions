package com.example.currencyconversions.domain.entity.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.JsonObject

@Entity(tableName = ConversionRatesDay.TABLE_NAME)
class ConversionRatesDay(
    @PrimaryKey @ColumnInfo(name = TABLE_PRIMARY_KEY_NAME) val date: Long,
    @ColumnInfo(name = "conversion_rates") val conversionRates: String
) {

    companion object {
        const val TABLE_NAME = "conversion_rates_table"
        const val TABLE_PRIMARY_KEY_NAME = "date_id"
    }

    @Ignore
    private var rates: JsonObject? = null

    fun getRates(): JsonObject {
        if (rates == null)
            rates = Gson().fromJson(conversionRates, JsonObject::class.java)
        return rates!!
    }
}