package com.example.currencyconversions.data.repo

import com.example.currencyconversions.domain.repo.CurrencyRepo
import com.example.currencyconversions.data.database.AppDatabaseDao
import com.example.currencyconversions.data.remote.APIService

class CurrencyRepoImpl(
    private val apiService: APIService,
    private val appDatabaseDao: AppDatabaseDao
) : CurrencyRepo {
}