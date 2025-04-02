package com.amineaytac.lunessa.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amineaytac.lunessa.core.common.ResponseState
import com.amineaytac.lunessa.core.domain.GetAllMakeupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getAllMakeupUseCase: GetAllMakeupUseCase) :
    ViewModel() {

    private val _makeupScreenUiState = MutableLiveData(HomeScreenUiState.initial())
    val makeupScreenUiState: LiveData<HomeScreenUiState> get() = _makeupScreenUiState

    fun getAllMakeupProducts() {
        viewModelScope.launch {
            getAllMakeupUseCase().collect { responseState ->
                when (responseState) {
                    is ResponseState.Error -> {
                        _makeupScreenUiState.postValue(
                            HomeScreenUiState(
                                isError = true,
                                errorMessage = responseState.message
                            )
                        )
                    }

                    is ResponseState.Loading -> {
                        _makeupScreenUiState.postValue(HomeScreenUiState(isLoading = true))
                    }

                    is ResponseState.Success -> {
                        _makeupScreenUiState.postValue(HomeScreenUiState(responseState.data))
                    }

                    else -> {}
                }
            }
        }
    }
}