package com.amineaytac.lunessa.core.network.rest

import com.amineaytac.lunessa.core.network.dto.MakeupResponse
import retrofit2.Response
import javax.inject.Inject

class RestDataSourceImpl @Inject constructor(private val makeupRestApi: MakeupRestApi) :
    RestDataSource {

    override suspend fun getAllMakeupProducts(): Response<MakeupResponse> {
        return makeupRestApi.getAllMakeupProducts()
    }
}