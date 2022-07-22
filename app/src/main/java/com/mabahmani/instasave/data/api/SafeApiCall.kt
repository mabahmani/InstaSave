package com.mabahmani.instasave.data.api

import android.content.Context
import com.mabahmani.instasave.data.api.response.Failure
import com.mabahmani.instasave.util.networkConnectionAvailable
import com.mabahmani.instasave.util.toApiFailureExceptions
import timber.log.Timber

suspend fun <T> safeApiCall(context: Context, apiCall: suspend () -> T): Result<T> {
    return try {
        if (context.networkConnectionAvailable()){
            Result.success(apiCall.invoke())
        }
        else{
            Result.failure(Failure.NetworkConnectionError)
        }
    }catch (ex: Exception){
        Result.failure(ex.toApiFailureExceptions())
    }
}
