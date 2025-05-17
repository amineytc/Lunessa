package com.amineaytac.lunessa.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.amineaytac.lunessa.databinding.FragmentFilterBinding

class FilterFragment : Fragment() {

    private lateinit var binding: FragmentFilterBinding
    private val viewModel: FilterViewModel by viewModels()
    private val defaultPriceRange = listOf(20f, 80f)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUI()
    }

    private fun setUI() = with(binding) {

        findNavController().currentBackStackEntry?.savedStateHandle?.let {
            viewModel.handleNavigationResult(it)
        }

        observeFilter(selected = viewModel.selectedBrand, button = btnBrand, label = "Brand")
        observeFilter(selected = viewModel.selectedTag, button = btnTag, label = "Tag")

        btnBrand.setOnClickListener { navigateToFilterItem("brand") }
        btnTag.setOnClickListener { navigateToFilterItem("tag") }
        ivClose.setOnClickListener { findNavController().popBackStack() }
        tvClear.setOnClickListener { resetFiltersUI() }
    }

    private fun observeFilter(selected: LiveData<String?>, button: Button, label: String) {
        selected.observe(viewLifecycleOwner) { value ->
            button.text = value?.let { "$label '$it' " } ?: label
        }
    }

    private fun navigateToFilterItem(type: String) {
        val action = FilterFragmentDirections.actionFilterFragmentToFilterItemFragment(type)
        findNavController().navigate(action)
    }

    private fun resetFiltersUI() = with(binding) {
        viewModel.resetFilters()
        rangeSlider.values = defaultPriceRange
    }
}