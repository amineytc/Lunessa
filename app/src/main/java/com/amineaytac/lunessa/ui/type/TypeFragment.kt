package com.amineaytac.lunessa.ui.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amineaytac.lunessa.databinding.FragmentTypeBinding

class TypeFragment : Fragment() {

    private lateinit var binding: FragmentTypeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTypeBinding.inflate(inflater, container, false)
        return binding.root
    }
}