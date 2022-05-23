package ru.guru.englearn2.View.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.Model.Topic
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Adapters.TopicAdapter
import ru.guru.englearn2.View.Interfaces.onLessonClickListener
import ru.guru.englearn2.databinding.FragmentLibraryBinding

class LibraryFragment : Fragment(R.layout.fragment_library), onLessonClickListener {

    private lateinit var binding: FragmentLibraryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)

        val realm = Realm.getDefaultInstance()

        binding.apply {
            val data = realm.where(Topic::class.java).findAll()
            val adapter = TopicAdapter(requireContext(), ArrayList(data), this@LibraryFragment)
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(context)
        }

        return binding.root
    }

    override fun onClick(lesson: Lesson) {

    }

}