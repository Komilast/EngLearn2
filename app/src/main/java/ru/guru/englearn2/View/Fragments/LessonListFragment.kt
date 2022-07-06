package ru.guru.englearn2.View.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import io.realm.RealmList
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Activities.WordListActivity
import ru.guru.englearn2.View.Adapters.LessonAdapter
import ru.guru.englearn2.View.Interfaces.onLessonClickListener
import ru.guru.englearn2.ViewModel.LessonListVM
import ru.guru.englearn2.databinding.FragmentLessonlistBinding

class LessonListFragment : Fragment(R.layout.fragment_lessonlist), onLessonClickListener {

    private lateinit var binding: FragmentLessonlistBinding
    private lateinit var adapter: LessonAdapter
    private lateinit var viewModel: LessonListVM
    private lateinit var lessons: LiveData<RealmList<Lesson>>
    private var idTopic: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLessonlistBinding.inflate(inflater, container, false)
        idTopic = requireActivity().intent.getIntExtra("idTopic", 0)
        viewModel = ViewModelProvider(this@LessonListFragment)[LessonListVM::class.java]
        lessons = viewModel.getAllLessons(idTopic)!!
        adapter = LessonAdapter(ArrayList(lessons.value!!), requireContext(), this, idTopic)

        binding.apply {
            recycler.adapter = adapter
            recycler.layoutManager = GridLayoutManager(requireContext(), 3)

            lessons.value!!.addChangeListener { t ->
                adapter.changeData(ArrayList(t))
            }
        }

        return binding.root
    }

    override fun onClick(lesson: Lesson) {
        val intent = Intent(requireContext(), WordListActivity::class.java)
        intent.putExtra("idLesson", lesson.id)
        intent.putExtra("idTopic", idTopic)
        startActivity(intent)
    }

    override fun onAddClick(idTopic: Int) {
        Log.d("My", "Zero")
    }

}