package ru.guru.englearn2.View.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Activities.WordListActivity
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
                onMenuClick.onClick(0)
            }
            del.setOnClickListener {
                val intent = Intent(requireContext(), WordListActivity::class.java)
                intent.putExtra("idLesson", -2)
                startActivity(intent)
            }
        }

        return binding.root
    }
}