package com.pluu.calladapter.sample.data

import com.pluu.retrofit.adapter.apiresult.ResultCallAdapterFactory
import logcat.logcat
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataSource {
    private val client = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor { message ->
                logcat { message }
            }.apply {
                this.setLevel(HttpLoggingInterceptor.Level.BASIC)
            }
        )
        .build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(ResultCallAdapterFactory())
        .build()

    val service: GitHubService by lazy {
        retrofit.create(GitHubService::class.java)
    }
}