package com.mabahmani.instasave.data.api.response

sealed class Failure(ex: Exception?): Exception(ex) {
    class InternalServerError(ex: Exception?) : Failure(ex)
    class HttpErrorBadRequest(ex: Exception?) : Failure(ex)
    class HttpErrorUnauthorized(ex: Exception?) : Failure(ex)
    class HttpError(ex: Exception?) : Failure(ex)
    class Error(ex: Exception?) : Failure(ex)
    object NetworkConnectionError : Failure(null)
}
