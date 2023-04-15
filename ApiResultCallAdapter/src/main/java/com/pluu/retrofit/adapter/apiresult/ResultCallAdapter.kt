package com.pluu.retrofit.adapter.apiresult

import com.pluu.retrofit.adapter.apiresult.error.ApiError
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.Type

internal class ResultCallAdapter<R>(
    retrofit: Retrofit,
    errorType: Type,
    private val bodyType: Type
) : CallAdapter<R, Call<ApiResult<R>>> {

    private val errorConverter: Converter<ResponseBody, ApiError> =
        retrofit.responseBodyConverter(errorType, arrayOfNulls(0))

    override fun adapt(call: Call<R>): Call<ApiResult<R>> =
        ApiResultCall(call, errorConverter, bodyType)

    override fun responseType(): Type = bodyType

    class ApiResultCall<E : ApiError, R>(
        private val original: Call<R>,
        private val errorConverter: Converter<ResponseBody, E>,
        private val bodyType: Type
    ) : Call<ApiResult<R>> {

        override fun enqueue(callback: Callback<ApiResult<R>>) {
            original.enqueue(object : Callback<R> {

                override fun onFailure(call: Call<R>, t: Throwable) {
                    callback.onFailure(this@ApiResultCall, t)
                }

                override fun onResponse(call: Call<R>, response: Response<R>) {
                    onResponseConvert(
                        callback,
                        this@ApiResultCall,
                        errorConverter,
                        bodyType,
                        response,
                        { body, _ ->
                            Response.success(response.code(), ApiResult.successOf(body))
                        },
                        { errorBody, _ ->
                            Response.success(ApiResult.errorOf(errorBody))
                        }
                    )
                }
            })
        }

        override fun clone(): Call<ApiResult<R>> =
            ApiResultCall(original.clone(), errorConverter, bodyType)

        override fun execute(): Response<ApiResult<R>> =
            throw UnsupportedOperationException("This adapter does not support sync execution")

        override fun isExecuted(): Boolean = original.isExecuted

        override fun cancel() {
            original.cancel()
        }

        override fun isCanceled(): Boolean = original.isCanceled

        override fun request(): Request = original.request()

        override fun timeout(): Timeout = original.timeout()
    }
}