package com.example.currencyconversions.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyconversions.domain.entity.database.ConversionRatesDay

@Dao
interface AppDatabaseDao {

    @Query("SELECT * FROM ${ConversionRatesDay.TABLE_NAME} WHERE ${ConversionRatesDay.TABLE_PRIMARY_KEY_NAME} = :date")
    fun getConversionRate(date: Long): ConversionRatesDay

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(conversionRatesDay: ConversionRatesDay)
}