package com.amineaytac.lunessa.ui.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.amineaytac.lunessa.databinding.FragmentTypeBinding
import com.amineaytac.lunessa.util.gone
import com.amineaytac.lunessa.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TypeFragment : Fragment() {

    private lateinit var binding: FragmentTypeBinding
    private val viewModel: TypeViewModel by viewModels()
    private lateinit var makeupTypeAdapter: MakeupTypeAdapter

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

        binding.floatingButton.setOnClickListener {
            binding.rvType.scrollToPosition(0)
        }
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
                    setRcyc()
                    makeupTypeAdapter.submitList(it.products)
                }
            }
        }
    }
}