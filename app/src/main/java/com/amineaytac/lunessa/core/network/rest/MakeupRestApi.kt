package com.amineaytac.lunessa.core.network.rest

import com.amineaytac.lunessa.core.network.dto.MakeupResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MakeupRestApi {
    @GET(".")
    suspend fun getAllMakeupProducts(): Response<MakeupResponse>

    @GET(".")
    suspend fun getMakeupProductsByType(
        @Query("product_type") productType: String
    ): Response<MakeupResponse>
}