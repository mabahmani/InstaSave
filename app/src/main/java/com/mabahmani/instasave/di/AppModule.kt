package com.mabahmani.instasave.di

import android.content.Context
import android.content.SharedPreferences
import com.mabahmani.instasave.R
import com.mabahmani.instasave.util.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences{
        return context.getSharedPreferences(AppConstants.Prefs.NAME, Context.MODE_PRIVATE)
    }
}