package com.pluu.retrofit.adapter.apiresult

import com.pluu.retrofit.adapter.apiresult.error.ApiError

class ApiResult<out R> {
    private var success: R? = null
    private var error: ApiError? = null

    constructor(success: R) {
        this.success = success
    }

    constructor(error: ApiError) {
        this.error = error
    }

    fun isSuccess(): Boolean = success != null

    fun isError(): Boolean = error != null

    fun result(): R {
        return requireNotNull(success)
    }

    fun error(): ApiError {
        return requireNotNull(error)
    }

    fun exceptionOrNull(): Throwable? = error?.safeThrowable()

    companion object {
        fun <R> successOf(result: R): ApiResult<R> = ApiResult(result)

        fun <R> errorOf(error: ApiError): ApiResult<R> = ApiResult(error)
    }
}

inline fun <T> ApiResult<T>.onFailure(action: (error: ApiError) -> Unit): ApiResult<T> {
    if (isError()) action(error())
    return this
}

inline fun <T> ApiResult<T>.onSuccess(action: (value: T) -> Unit): ApiResult<T> {
    if (isSuccess()) action(result())
    return this
}

internal fun <T> ApiError.toApiResult(): ApiResult<T> = ApiResult.errorOf(this)