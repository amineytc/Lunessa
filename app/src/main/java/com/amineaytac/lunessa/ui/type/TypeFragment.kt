package com.amineaytac.lunessa.ui.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.GridLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amineaytac.lunessa.R
import com.amineaytac.lunessa.databinding.FragmentTypeBinding
import com.amineaytac.lunessa.util.gone
import com.amineaytac.lunessa.util.visible
import com.google.android.material.chip.Chip
import com.yagmurerdogan.toasticlib.Toastic
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TypeFragment : Fragment() {

    private lateinit var binding: FragmentTypeBinding
    private val viewModel: TypeViewModel by viewModels()
    private lateinit var makeupTypeAdapter: MakeupTypeAdapter
    private var selectedItem: String? = null
    private var selectedFilterType: String? = null
    private var selectedChip: Chip? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productType = arguments?.getString("productType") ?: ""

        viewModel.getMakeupProductsByType(productType)
        observeUi()
        setRcyc()
        setupClickListeners()
        setupSearchView()
        setupApplyButton()
    }

    private fun setupChipClick(chip: Chip, type: String) = with(binding) {
        chip.setOnClickListener {
            selectedChip = chip
            floatingButton.gone()
            toggleFilterView(type, chip)
        }
    }

    private fun Chip.setDefaultStyle() {
        chipStrokeColor = ContextCompat.getColorStateList(requireContext(), R.color.pink_flare)
        chipIconTint = ContextCompat.getColorStateList(requireContext(), R.color.pink_flare)
        setChipIconResource(R.drawable.ic_down)
    }

    private fun Chip.setSelectedStyle() {
        chipStrokeColor = ContextCompat.getColorStateList(requireContext(), R.color.pinkish_red)
        chipIconTint = ContextCompat.getColorStateList(requireContext(), R.color.pinkish_red)
        setChipIconResource(R.drawable.ic_up)
    }

    private fun setupScrollListener() {
        binding.rvType.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                if (dy > 0) binding.floatingButton.visible()
                else if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    binding.floatingButton.gone()
                }
            }
        })
    }

    private fun setupClickListeners() = with(binding) {
        floatingButton.setOnClickListener { rvType.scrollToPosition(0) }

        setupChipClick(chip, "brand")
        setupChipClick(chipOne, "tag")
        chipThree.setOnClickListener { findNavController().navigate(R.id.action_typeFragment_to_filterFragment) }

        dimView.setOnClickListener {
            filterLayout.gone()
            dimView.gone()
            selectedChip?.setDefaultStyle()
            selectedChip = null
            selectedFilterType = null
        }

        setupScrollListener()
    }

    private fun updateCheckboxes(items: List<String>, filter: String = "") {
        val filteredItems = items.filter { it.contains(filter, ignoreCase = true) }
        binding.checkboxContainer.removeAllViews()

        for (item in filteredItems) {
            val checkBox = CheckBox(requireContext()).apply {
                text = item
                isChecked = selectedItem == item
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(8, 8, 8, 8)
                }
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedItem = item
                    } else if (selectedItem == item) {
                        selectedItem = null
                    }
                    updateCheckboxes(items)
                }
            }
            binding.checkboxContainer.addView(checkBox)
        }
    }

    private fun setupSearchView() = with(binding) {
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                val items = when (selectedFilterType) {
                    "brand" -> resources.getStringArray(R.array.brand_list).toList()
                    "tag" -> resources.getStringArray(R.array.tag_list).toList()
                    else -> emptyList()
                }

                updateCheckboxes(items, newText.orEmpty())
                return true
            }
        })
    }

    private fun setupApplyButton() {
        binding.btnApply.setOnClickListener {
            if (selectedItem != null) {
                //
            } else {
                Toastic.toastic(
                    context = requireContext(),
                    message = getString(R.string.no_item),
                    duration = Toastic.LENGTH_SHORT,
                    type = Toastic.WARNING,
                    isIconAnimated = true
                ).show()
            }
        }
    }

    private fun toggleFilterView(type: String, chip: Chip) = with(binding) {
        if (selectedFilterType == type) {
            filterLayout.gone()
            dimView.gone()
            chip.setDefaultStyle()
            selectedFilterType = null
        } else {
            resetChips()

            val items = if (type == "brand") {
                resources.getStringArray(R.array.brand_list).toList()
            } else {
                resources.getStringArray(R.array.tag_list).toList()
            }

            updateCheckboxes(items)
            chip.setSelectedStyle()
            filterLayout.visible()
            dimView.visible()
            filterLayout.bringToFront()
            selectedFilterType = type
        }
    }

    private fun resetChips() = with(binding) {
        chip.setDefaultStyle()
        chipOne.setDefaultStyle()
    }

    private fun setRcyc() = with(binding) {
        makeupTypeAdapter = MakeupTypeAdapter(
            onItemClickListener = {

            }
        )
        rvType.layoutManager = GridLayoutManager(requireContext(), 2)
        rvType.setHasFixedSize(true)
        rvType.adapter = makeupTypeAdapter
    }

    private fun observeUi() = with(binding) {
        viewModel.typeScreenUiState.observe(viewLifecycleOwner) {
            when {
                it.isLoading -> {
                    pbType.visible()
                }

                it.isError -> {
                    rvType.gone()
                    pbType.gone()
                    ivError.visible()
                    tvError.visible()
                    tvError.text = it.errorMessage
                }

                else -> {
                    pbType.gone()
                    makeupTypeAdapter.submitList(it.products)
                }
            }
        }
    }
}