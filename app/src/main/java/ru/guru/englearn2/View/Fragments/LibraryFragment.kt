package ru.guru.englearn2.View.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.Model.Topic
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Adapters.TopicAdapter
import ru.guru.englearn2.View.Interfaces.StartFragment
import ru.guru.englearn2.View.Interfaces.onLessonClickListener
import ru.guru.englearn2.ViewModel.LibraryVM
import ru.guru.englearn2.databinding.FragmentLibraryBinding
import kotlin.math.log

class LibraryFragment : Fragment(R.layout.fragment_library), onLessonClickListener {

    private lateinit var binding: FragmentLibraryBinding
    private var viewModel: LibraryVM? = null
    private var topics: LiveData<ArrayList<Topic>>? = null
    private lateinit var adapter: TopicAdapter
    private lateinit var startFragment: StartFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        startFragment = context as StartFragment
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View  = runBlocking{
        val start = System.nanoTime()
        binding = FragmentLibraryBinding.inflate(inflater)

        binding.apply {
            adapter = TopicAdapter(requireContext(), ArrayList(), this@LibraryFragment)
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(context)
        }
        launch {
            if (viewModel == null) viewModel = ViewModelProvider(this@LibraryFragment)[LibraryVM::class.java]
            if (topics == null) topics = viewModel!!.getAllTopics()
            topics!!.observe(this@LibraryFragment){
                adapter.setData(it)
            }
        }

        Log.d("My", "SpeedTest: LibraryFragment/onCreateView - finish: ${(System.nanoTime() - start) / 1000000}ms")
        return@runBlocking binding.root
    }

    override fun onClick(lesson: Lesson) {
        startFragment.startWordList(lesson)
    }

}