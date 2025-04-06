package com.amineaytac.lunessa.core.data.repo

import com.amineaytac.lunessa.core.common.ResponseState
import com.amineaytac.lunessa.core.data.model.Makeup
import kotlinx.coroutines.flow.Flow

interface MakeupRepository {
    suspend fun getAllMakeupProducts(): Flow<ResponseState<List<Makeup>>>

    suspend fun getMakeupProductsByType(productType: String): Flow<ResponseState<List<Makeup>>>
}