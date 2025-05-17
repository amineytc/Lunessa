package com.amineaytac.lunessa.ui.filteritem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.amineaytac.lunessa.R
import com.amineaytac.lunessa.core.data.repo.toFilterItemList
import com.amineaytac.lunessa.databinding.FragmentFilterItemBinding
import com.amineaytac.lunessa.util.hideKeyboard
import com.yagmurerdogan.toasticlib.Toastic

class FilterItemFragment : Fragment() {

    private lateinit var binding: FragmentFilterItemBinding
    private lateinit var filterItemAdapter: FilterItemAdapter
    private val viewModel: FilterItemViewModel by viewModels()
    private val args: FilterItemFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRcy()
        observeViewModel()
        setupSearchView()
        bindUI()
    }

    private fun bindUI() = with(binding) {

        ivBack.setOnClickListener { findNavController().popBackStack() }

        val items = when (args.filterType) {
            "brand" -> requireContext().resources.getStringArray(R.array.brand_list).toList()
            "tag" -> requireContext().resources.getStringArray(R.array.tag_list).toList()
            else -> emptyList()
        }

        viewModel.setFullList(items.toFilterItemList())

        tvName.text = when (args.filterType) {
            "brand" -> getString(R.string.brand)
            "tag" -> getString(R.string.tag)
            else -> getString(R.string.filter)
        }

        btnApply.setOnClickListener {
            val selected = viewModel.getSelectedItem()
            if (selected != null) {
                findNavController().previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("selected_item", selected)
                findNavController().previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("type", args.filterType)
                findNavController().popBackStack()
            } else {
                Toastic.toastic(
                    context = requireContext(),
                    message = getString(R.string.selection),
                    duration = Toastic.LENGTH_SHORT,
                    type = Toastic.INFO,
                    isIconAnimated = true
                ).show()
            }
        }
    }

    private fun setupSearchView() = with(binding) {
        svFilter.setOnClickListener { svFilter.isIconified = false }

        svFilter.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus) svFilter.isIconified = true
        }

        svFilter.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filter(newText.orEmpty())

                if (newText.isNullOrEmpty()) {
                    svFilter.post {
                        svFilter.clearFocus()
                        svFilter.hideKeyboard()
                        rvFilter.scrollToPosition(0)
                    }
                }
                return true
            }
        })
    }

    private fun observeViewModel() {
        viewModel.filterItems.observe(viewLifecycleOwner) { itemList ->
            filterItemAdapter.submitList(itemList)
        }
    }

    private fun setRcy() = with(binding) {
        filterItemAdapter = FilterItemAdapter { selectedItem ->
            viewModel.selectItem(selectedItem)
        }
        rvFilter.layoutManager = LinearLayoutManager(requireContext())
        rvFilter.setHasFixedSize(false)
        rvFilter.adapter = filterItemAdapter
    }
}