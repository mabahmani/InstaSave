package com.mabahmani.instasave.util

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.mabahmani.instasave.data.api.response.Failure
import io.ktor.client.features.*
import java.text.SimpleDateFormat
import java.util.*


fun Long?.orZero(): Long{
    return this ?: 0
}

fun Int?.orZero(): Int{
    return this ?: 0
}

fun Exception.toApiFailureExceptions() = when (this) {
    is ServerResponseException -> Failure.InternalServerError(this)
    is ClientRequestException ->
        when (this.response.status.value) {
            400 -> Failure.HttpErrorBadRequest(this)
            401 -> Failure.HttpErrorUnauthorized(this)
            403 -> Failure.HttpErrorUnauthorized(this)
            else -> Failure.HttpError(this)
        }
    else -> Failure.Error(this)
}

fun Context.networkConnectionAvailable(): Boolean{
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun Long.timeStampToHumanReadable(): String{
    return SimpleDateFormat("EEE, d MMM yyyy", Locale.US).format(this)
}

fun Context.toast(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Int.toPercentString(): String{
    return "$this%"
}