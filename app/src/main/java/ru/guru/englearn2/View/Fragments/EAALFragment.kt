package ru.guru.englearn2.View.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Activities.WordListActivity
import ru.guru.englearn2.ViewModel.EAALVM
import ru.guru.englearn2.databinding.FragmentEaalBinding
import java.io.File

class EAALFragment : Fragment(R.layout.fragment_eaal) {

    private lateinit var binding: FragmentEaalBinding
    private lateinit var lesson: Lesson
    private lateinit var viewModel: EAALVM
    private var idLesson: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEaalBinding.inflate(inflater, container, false)
        idLesson = requireActivity().intent.getIntExtra("idLesson", 0)
        viewModel = ViewModelProvider(this)[EAALVM::class.java]
        lesson = viewModel.getLesson(idLesson)
        val lessonTemp = "${lesson.title}.png"

        binding.apply {

            editTitle.addTextChangedListener {
                if (editTitle.text?.length == 0) titleEditLayout.error = "Поле не должно быть пустым"
                else titleEditLayout.error = null
            }

            okBtn.setOnClickListener {
                if (editTitle.text?.length != 0){
                    viewModel.saveLesson(idLesson, editTitle.text!!.toString())
                    val dirImages = File(requireContext().filesDir.path, "images")
                    File(dirImages, lessonTemp).renameTo(File(dirImages, "${lesson.title}.png"))
//                    val intent = Intent(requireContext(), WordListActivity::class.java)
//                    intent.putExtra("idLesson", idLesson)
//                    startActivity(intent)
                } else Toast.makeText(requireContext(), "Поле заголовка пустое", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }

            /** Режим редактирования */

            if(idLesson != -1){
                editTitle.setText(lesson.title)
            }
        }

        return binding.root
    }

}