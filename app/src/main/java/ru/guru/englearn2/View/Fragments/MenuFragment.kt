package ru.guru.englearn2.View.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Interfaces.OnMenuClick
import ru.guru.englearn2.databinding.FragmentMenuBinding

class MenuFragment : Fragment(R.layout.fragment_menu) {

    private lateinit var binding: FragmentMenuBinding
    private val menu2Fragment = Menu2Fragment()
    private lateinit var onMenuClick: OnMenuClick

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onMenuClick = context as OnMenuClick
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        binding.apply {
            fav.setOnClickListener {
                onMenuClick.onClick(-1)
            }
            del.setOnClickListener {
                onMenuClick.onClick(-2)
            }
        }

        return binding.root
    }
}