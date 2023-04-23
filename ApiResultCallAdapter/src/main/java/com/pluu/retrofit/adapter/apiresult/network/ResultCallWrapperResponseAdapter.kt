package com.pluu.retrofit.adapter.apiresult.network

import com.pluu.retrofit.adapter.apiresult.ApiResult
import com.pluu.retrofit.adapter.apiresult.ResponseW
import com.pluu.retrofit.adapter.apiresult.error.ApiError
import com.pluu.retrofit.adapter.apiresult.onResponseConvert
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

internal class ResultCallWrapperResponseAdapter<R>(
    retrofit: Retrofit,
    errorType: Type,
    private val bodyType: Type
) : CallAdapter<R, Call<ResponseW<R>>> {

    private val errorConverter: Converter<ResponseBody, ApiError> =
        retrofit.responseBodyConverter(errorType, arrayOfNulls(0))

    override fun adapt(call: Call<R>): Call<ResponseW<R>> =
        ResponseWCall(call, errorConverter, bodyType)

    override fun responseType(): Type = bodyType

    class ResponseWCall<E : ApiError, R>(
        private val original: Call<R>,
        private val errorConverter: Converter<ResponseBody, E>,
        private val bodyType: Type
    ) : Call<ResponseW<R>> {

        override fun enqueue(callback: Callback<ResponseW<R>>) {
            original.enqueue(object : Callback<R> {

                override fun onFailure(call: Call<R>, t: Throwable) {
                    callback.onFailure(this@ResponseWCall, t)
                }

                override fun onResponse(call: Call<R>, response: Response<R>) {
                    onResponseConvert(
                        callback,
                        this@ResponseWCall,
                        errorConverter,
                        bodyType,
                        response,
                        { body, responseT ->
                            Response.success(
                                responseT.code(),
                                ResponseW(responseT.raw(), ApiResult.successOf(body))
                            )
                        },
                        { errorBody, responseV ->
                            Response.success(
                                ResponseW(
                                    responseV.raw(),
                                    ApiResult.errorOf(errorBody)
                                )
                            )
                        }
                    )
                }
            })
        }

        override fun clone(): Call<ResponseW<R>> =
            ResponseWCall(original.clone(), errorConverter, bodyType)

        override fun execute(): Response<ResponseW<R>> =
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