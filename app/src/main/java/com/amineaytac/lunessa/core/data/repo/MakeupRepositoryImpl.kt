package com.amineaytac.lunessa.core.data.repo

import com.amineaytac.lunessa.core.common.ResponseState
import com.amineaytac.lunessa.core.data.model.Makeup
import com.amineaytac.lunessa.core.network.rest.RestDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MakeupRepositoryImpl @Inject constructor(private val restDataSource: RestDataSource) :
    MakeupRepository {

    override suspend fun getAllMakeupProducts(): Flow<ResponseState<List<Makeup>>> {
        return flow {
            emit(ResponseState.Loading)
            val response = restDataSource.getAllMakeupProducts()
            emit(ResponseState.Success(response.mapTo { it.toMakeupList() }))
        }.catch {
            emit(ResponseState.Error(it.message.orEmpty()))
        }
    }

    override suspend fun getMakeupProductsByType(productType: String): Flow<ResponseState<List<Makeup>>> {
        return flow {
            emit(ResponseState.Loading)
            val response = restDataSource.getMakeupProductsByType(productType)
            emit(ResponseState.Success(response.mapTo { it.toMakeupList() }))
        }
    }
}