package ru.guru.englearn2.View.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.guru.englearn2.R
import ru.guru.englearn2.databinding.FragmentLessonlistBinding

class LessonListFragment : Fragment(R.layout.fragment_lessonlist) {

    private lateinit var binding: FragmentLessonlistBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLessonlistBinding.inflate(inflater, container, false)

        return binding.root
    }

}