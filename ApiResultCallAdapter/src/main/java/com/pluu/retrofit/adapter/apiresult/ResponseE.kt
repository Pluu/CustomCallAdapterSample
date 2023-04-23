package com.pluu.retrofit.adapter.apiresult

import okhttp3.Headers
import okhttp3.Response

data class ResponseE<R>(
    val raw: Response,
    val body: ApiResult<R>
) {
    val code: Int = raw.code()

    val message: String? = raw.message()

    val headers: Headers = raw.headers()

    val isSuccessful: Boolean = raw.isSuccessful
}