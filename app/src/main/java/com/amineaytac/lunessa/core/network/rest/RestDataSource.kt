package com.amineaytac.lunessa.core.network.rest

import com.amineaytac.lunessa.core.network.dto.MakeupResponse
import retrofit2.Response

interface RestDataSource {
    suspend fun getAllMakeupProducts(): Response<MakeupResponse>
}