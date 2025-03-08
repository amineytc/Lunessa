package com.amineaytac.lunessa.ui.onboard.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amineaytac.lunessa.R
import com.amineaytac.lunessa.databinding.FragmentThirdBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirdFragment : Fragment() {

    private lateinit var binding: FragmentThirdBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.finish.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment2)
            onBoardingIsFinished()
        }
    }

    private fun onBoardingIsFinished() {
        val sharedPreferences =
            requireActivity().getSharedPreferences(
                getString(R.string.on_boarding),
                Context.MODE_PRIVATE
            )
        val editor = sharedPreferences.edit()
        editor.putBoolean(getString(R.string.finished), true)
        editor.apply()
    }
}