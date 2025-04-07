package com.amineaytac.lunessa.ui.type

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amineaytac.lunessa.core.common.ResponseState
import com.amineaytac.lunessa.core.domain.GetMProductsByTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TypeViewModel @Inject constructor(private val getMProductsByTypeUseCase: GetMProductsByTypeUseCase) :
    ViewModel() {

    private val _typeScreenUiState = MutableLiveData(TypeScreenUiState.initial())
    val typeScreenUiState: LiveData<TypeScreenUiState> get() = _typeScreenUiState

    fun getMakeupProductsByType(productType: String) {
        viewModelScope.launch {
            getMProductsByTypeUseCase(productType).collect { responseState ->
                when (responseState) {
                    is ResponseState.Error -> {
                        _typeScreenUiState.postValue(
                            TypeScreenUiState(
                                isError = true,
                                errorMessage = responseState.message
                            )
                        )
                    }

                    is ResponseState.Loading -> {
                        _typeScreenUiState.postValue(TypeScreenUiState(isLoading = true))
                    }

                    is ResponseState.Success -> {
                        _typeScreenUiState.postValue(TypeScreenUiState(responseState.data))
                    }

                    else -> {}
                }
            }
        }
    }
}