package com.pluu.calladapter.sample.data

import com.pluu.calladapter.sample.data.model.User
import com.pluu.retrofit.adapter.apiresult.ApiResult
import retrofit2.http.GET

interface GitHubService {
    @GET("/users/Pluu")
    suspend fun suspendGetUser(): ApiResult<User>

    @GET("/error")
    suspend fun suspendTryNetworkError(): ApiResult<User>
}