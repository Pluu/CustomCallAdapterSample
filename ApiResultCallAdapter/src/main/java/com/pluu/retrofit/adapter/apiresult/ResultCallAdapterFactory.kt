package com.pluu.retrofit.adapter.apiresult

import com.pluu.retrofit.adapter.apiresult.error.ApiError
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)

        if (returnType !is ParameterizedType) {
            val name = parseTypeName(returnType)
            throw IllegalArgumentException(
                "Return type must be parameterized as " +
                        "$name<Foo> or $name<out Foo>"
            )
        }

        return when (rawType) {
            Call::class.java -> apiResultAdapter(returnType)
            else -> null
        }
    }

    private fun apiResultAdapter(
        returnType: ParameterizedType
    ): CallAdapter<Type, out Call<out Any>>? {
        val wrapperType = getParameterUpperBound(0, returnType)
        return when (getRawType(wrapperType)) {
            ApiResult::class.java -> {
                val (_, bodyType) = extractErrorAndReturnType(wrapperType, returnType)
                ApiResultCallAdapter(bodyType)
            }
            else -> null
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun extractErrorAndReturnType(
        wrapperType: Type,
        returnType: ParameterizedType
    ): Pair<Type, Type> {
        if (wrapperType !is ParameterizedType) {
            val name = parseTypeName(returnType)
            throw IllegalArgumentException(
                "Return type must be parameterized as " +
                        "$name<ErrorBody, ResponseBody> or $name<out ErrorBody, out ResponseBody>"
            )
        }
        return ApiError::class.java to getParameterUpperBound(0, wrapperType)
    }
}

private fun parseTypeName(type: Type) =
    type.toString()
        .split(".")
        .last()