package ru.guru.englearn2.View.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.guru.englearn2.R
import ru.guru.englearn2.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.apply {

        }


        return binding.root
    }

}