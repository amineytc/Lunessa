package com.amineaytac.lunessa.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {

    private val _selectedBrand = MutableLiveData<String?>()
    val selectedBrand: LiveData<String?> get() = _selectedBrand

    private val _selectedTag = MutableLiveData<String?>()
    val selectedTag: LiveData<String?> get() = _selectedTag

    private fun setSelectedItem(type: String, item: String) {
        when (type.lowercase()) {
            "brand" -> _selectedBrand.value = item
            "tag" -> _selectedTag.value = item
        }
    }

    fun resetFilters() {
        _selectedBrand.value = null
        _selectedTag.value = null
    }

    fun handleNavigationResult(savedStateHandle: SavedStateHandle) {
        val selectedItem = savedStateHandle.get<String>("selected_item")
        val type = savedStateHandle.get<String>("type")

        if (!selectedItem.isNullOrBlank() && !type.isNullOrBlank()) {
            setSelectedItem(type, selectedItem)
            savedStateHandle.remove<String>("selected_item")
            savedStateHandle.remove<String>("type")
        }
    }
}