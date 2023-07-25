package com.example.currencyconversions.data.di

import android.content.Context
import com.example.currencyconversions.data.database.AppDatabase
import com.example.currencyconversions.data.database.AppDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideYourDatabase(@ApplicationContext app: Context) = AppDatabase.getDatabase(app)

    @Provides
    fun provideChannelDao(appDatabase: AppDatabase): AppDatabaseDao {
        return appDatabase.appDatabaseDao()
    }
}