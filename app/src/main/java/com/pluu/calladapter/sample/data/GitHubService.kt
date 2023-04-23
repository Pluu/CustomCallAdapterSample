package com.pluu.calladapter.sample.data

import com.pluu.calladapter.sample.data.model.User
import com.pluu.retrofit.adapter.ApiResult
import retrofit2.http.GET

interface GitHubService {

    @GET("/users/Pluu")
    suspend fun getUserDefault(): User

    @GET("/error")
    suspend fun tryNetworkErrorDefault(): User

    @GET("/users/Pluu")
    suspend fun getUser(): ApiResult<User>

    @GET("/error")
    suspend fun tryNetworkError(): ApiResult<User>
}