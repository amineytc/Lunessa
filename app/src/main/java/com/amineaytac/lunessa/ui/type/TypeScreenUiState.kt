package com.amineaytac.lunessa.ui.type

import com.amineaytac.lunessa.core.data.model.Makeup

data class TypeScreenUiState(
    val products: List<Makeup> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = ""
) {
    companion object {
        fun initial() = TypeScreenUiState(isLoading = true)
    }
}