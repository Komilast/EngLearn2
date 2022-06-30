package ru.guru.englearn2.View.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.guru.englearn2.R
import ru.guru.englearn2.databinding.FragmentMenu2Binding

class Menu2Fragment : Fragment(R.layout.fragment_menu2) {

    private lateinit var binding: FragmentMenu2Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMenu2Binding.inflate(inflater, container, false)

        return binding.root
    }

}