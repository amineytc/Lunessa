package com.amineaytac.lunessa.core.network.rest

import com.amineaytac.lunessa.core.network.dto.MakeupResponse
import retrofit2.Response
import retrofit2.http.GET

interface MakeupRestApi {
    @GET(".")
    suspend fun getAllMakeupProducts(): Response<MakeupResponse>
}