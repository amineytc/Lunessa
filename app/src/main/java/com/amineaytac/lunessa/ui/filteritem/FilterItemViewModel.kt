package com.amineaytac.lunessa.ui.filteritem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amineaytac.lunessa.core.data.model.FilterItem
import com.amineaytac.lunessa.core.data.repo.markSelected

class FilterItemViewModel : ViewModel() {

    private var fullItemList: List<FilterItem> = emptyList()

    private val _filterItems = MutableLiveData<List<FilterItem>>()
    val filterItems: LiveData<List<FilterItem>> get() = _filterItems

    fun setFullList(list: List<FilterItem>) {
        fullItemList = list
        _filterItems.value = list
    }

    fun filter(query: String) {
        _filterItems.value = fullItemList
            .filter { it.title.contains(query, ignoreCase = true) }
    }

    fun selectItem(title: String) {
        fullItemList = fullItemList.markSelected(title)
        _filterItems.value = _filterItems.value?.markSelected(title)
    }

    fun getSelectedItem(): String? {
        return fullItemList.find { it.isSelected }?.title
    }
}