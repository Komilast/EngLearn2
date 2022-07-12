package ru.guru.englearn2.View.Fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import io.realm.RealmList
import ru.guru.englearn.database.LiveRealmObject
import ru.guru.englearn2.Helpers.OutlineSpan
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.Model.Topic
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Activities.WordListActivity
import ru.guru.englearn2.View.Adapters.LessonAdapter
import ru.guru.englearn2.View.Interfaces.onLessonClickListener
import ru.guru.englearn2.ViewModel.LessonListVM
import ru.guru.englearn2.databinding.FragmentLessonlistBinding
import java.io.File

class LessonListFragment : Fragment(R.layout.fragment_lessonlist), onLessonClickListener {

    private lateinit var binding: FragmentLessonlistBinding
    private lateinit var adapter: LessonAdapter
    private lateinit var viewModel: LessonListVM
    private lateinit var lessons: LiveData<RealmList<Lesson>>
    private var topic: LiveRealmObject<Topic>? = null
    private var idTopic: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLessonlistBinding.inflate(inflater, container, false)
        idTopic = requireActivity().intent.getIntExtra("idTopic", 0)
        viewModel = ViewModelProvider(this@LessonListFragment)[LessonListVM::class.java]
        lessons = viewModel.getAllLessons(idTopic)!!
        adapter = LessonAdapter(ArrayList(lessons.value!!), requireContext(), this, idTopic)
        topic = viewModel.getTopic(idTopic)
        Log.d("My", topic?.value.toString())

        binding.apply {
            recycler.adapter = adapter
            recycler.layoutManager = GridLayoutManager(requireContext(), 3)

            lessons.value!!.addChangeListener { t ->
                adapter.changeData(ArrayList(t))
            }

            val outlineSpan = OutlineSpan(Color.BLACK, 4F)
            var spannable: SpannableString

            when {
                idTopic >= 0 -> {
                    topicImage.setImageDrawable(
                        Drawable.createFromPath(
                            File(
                                "${requireContext().filesDir.path}/images/topics",
                                "${topic!!.value!!.title}_${topic!!.value!!.id}.png"
                            ).path
                        )
                    )
                    spannable = SpannableString(topic!!.value!!.title)
                    spannable.setSpan(outlineSpan, 0, topic!!.value!!.title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    head.title = spannable
                }
                idTopic == -1 -> {
                    head.title = "Избранное"
                }
                idTopic == -2 -> {
                    head.title = "Удалённое"
                }

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