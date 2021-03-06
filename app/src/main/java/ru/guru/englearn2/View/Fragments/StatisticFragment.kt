package ru.guru.englearn2.View.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.guru.englearn2.R
import ru.guru.englearn2.databinding.FragmentStatisticBinding

class StatisticFragment : Fragment(R.layout.fragment_statistic) {

    private lateinit var binding: FragmentStatisticBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }
}