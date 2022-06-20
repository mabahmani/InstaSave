package com.mabahmani.instasave.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mabahmani.instasave.R
import com.mabahmani.instasave.data.api.ApiService
import com.mabahmani.instasave.data.db.AppDatabase
import com.mabahmani.instasave.data.db.dao.DownloadDao
import com.mabahmani.instasave.util.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences{
        return context.getSharedPreferences(AppConstants.Prefs.NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideKtorHttpClient(sharedPreferences: SharedPreferences): HttpClient {
        return HttpClient(Android){
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })

                engine {
                    connectTimeout = 30_000
                    socketTimeout = 30_000
                }
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.tag("Logger Ktor =>").v(message)
                    }

                }
                level = LogLevel.ALL
            }

            install(ResponseObserver) {
                onResponse { response ->
                    Timber.tag("HTTP status:").d(response.status.value.toString())
                }
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Cookie, sharedPreferences.getString(AppConstants.Prefs.COOKIE, ""))
                header(HttpHeaders.UserAgent, "Instagram 238.0.0.14.112 Mozilla/5.0 (Linux; Android 12; SM-G981B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.162 Mobile Safari/537.36")
            }
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "instaSave-db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDownloadDao(db: AppDatabase): DownloadDao {
        return db.downloadDao()
    }

}