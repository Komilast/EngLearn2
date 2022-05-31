package ru.guru.englearn2.View.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Activities.WordListActivity
import ru.guru.englearn2.databinding.FragmentMenuBinding

class MenuFragment : Fragment(R.layout.fragment_menu) {

    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        binding.apply {
            favWord.setOnClickListener {
                val intent = Intent(requireContext(), WordListActivity::class.java)
                intent.putExtra("idLesson", -1)
                startActivity(intent)
            }
        }

        return binding.root
    }
}