package ru.guru.englearn2.View.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.RealmResults
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.Model.Topic
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Activities.EAALActivity
import ru.guru.englearn2.View.Activities.LessonListActivity
import ru.guru.englearn2.View.Activities.WordListActivity
import ru.guru.englearn2.View.Adapters.TopicAdapter
import ru.guru.englearn2.View.Interfaces.OnClickLibrary
import ru.guru.englearn2.View.Interfaces.onLessonClickListener
import ru.guru.englearn2.ViewModel.LibraryVM
import ru.guru.englearn2.databinding.FragmentLibraryBinding

class LibraryFragment : Fragment(R.layout.fragment_library), onLessonClickListener, OnClickLibrary {

    private lateinit var binding: FragmentLibraryBinding
    private var viewModel: LibraryVM? = null
    private var topics: RealmResults<Topic>? = null
    private lateinit var adapter: TopicAdapter

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View  = runBlocking{
        binding = FragmentLibraryBinding.inflate(inflater)

        binding.apply {
            adapter = TopicAdapter(requireContext(), ArrayList(), this@LibraryFragment, this@LibraryFragment)
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(context)
        }
        launch {
            if (viewModel == null) viewModel = ViewModelProvider(this@LibraryFragment)[LibraryVM::class.java]
            if (topics == null) topics = viewModel!!.getAllTopics()
            adapter.setData(ArrayList(topics!!))
            topics!!.addChangeListener { t -> adapter.setData(ArrayList(t)) }
        }

        return@runBlocking binding.root
    }

    override fun onClick(lesson: Lesson) {
        val intent = Intent(requireContext(), WordListActivity::class.java)
        intent.putExtra("idLesson", lesson.id!!)
        intent.putExtra("idTopic", lesson.topic!!.id)
        startActivity(intent)
    }

    override fun onAddClick(idTopic: Int) {
        val intent = Intent(requireContext(), EAALActivity::class.java)
        intent.putExtra("idTopic", idTopic)
        intent.putExtra("idLesson", -1)
        startActivity(intent)
    }

    override fun onClickTopic(topic: Topic) {
        val intent = Intent(requireContext(), LessonListActivity::class.java)
        intent.putExtra("idTopic", topic.id)
        startActivity(intent)
    }

}