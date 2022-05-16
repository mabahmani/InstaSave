package com.mabahmani.instasave.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.mabahmani.instasave.R
import com.mabahmani.instasave.data.api.ApiService
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
                header(HttpHeaders.UserAgent, "Instagram 230.0.0.20.108")
            }
        }
    }

}