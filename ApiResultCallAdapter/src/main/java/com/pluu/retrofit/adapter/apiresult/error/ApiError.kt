package com.pluu.retrofit.adapter.apiresult.error

sealed class ApiError {
    data class HttpError(val code: Int, val message: String, val body: String) : ApiError()

    data class NetworkError(val throwable: Throwable) : ApiError()

    data class UnknownApiError(val throwable: Throwable) : ApiError()

    fun safeThrowable(): Throwable = when (this) {
        is HttpError -> IllegalStateException("$message $body")
        is NetworkError -> throwable
        is UnknownApiError -> throwable
    }
}