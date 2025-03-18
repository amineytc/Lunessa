package com.amineaytac.lunessa.ui.home

import com.amineaytac.lunessa.core.data.model.Makeup

data class HomeScreenUiState(
    val products: List<Makeup> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = ""
) {
    companion object {
        fun initial() = HomeScreenUiState(isLoading = true)
    }
}