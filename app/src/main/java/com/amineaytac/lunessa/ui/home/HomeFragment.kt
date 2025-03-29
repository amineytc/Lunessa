package com.amineaytac.lunessa.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amineaytac.lunessa.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callInitialViewModelFunctions()
        observeUi()
    }

    private fun callInitialViewModelFunctions() {
        viewModel.getAllMakeupProducts()
    }

    private fun observeUi() {
        viewModel.makeupScreenUiState.observe(viewLifecycleOwner) {
            when {
                it.isError -> {
                    Log.d("home_data3", it.errorMessage.toString())
                }

                it.isLoading -> {
                    Log.d("home_data2", "loading")
                }

                else -> {
                    Log.d("home_data1", it.products.toString())
                }
            }
        }
    }
}