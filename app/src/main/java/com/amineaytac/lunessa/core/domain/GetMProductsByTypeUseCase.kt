package com.amineaytac.lunessa.core.domain

import com.amineaytac.lunessa.core.common.ResponseState
import com.amineaytac.lunessa.core.data.model.Makeup
import com.amineaytac.lunessa.core.data.repo.MakeupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMProductsByTypeUseCase @Inject constructor(private val makeupRepository: MakeupRepository) {
    suspend operator fun invoke(productType: String): Flow<ResponseState<List<Makeup>>> {
        return makeupRepository.getMakeupProductsByType(productType)
    }
}