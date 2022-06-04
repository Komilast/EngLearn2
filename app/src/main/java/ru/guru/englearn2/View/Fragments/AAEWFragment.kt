package ru.guru.englearn2.View.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.guru.englearn2.Model.Word
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Activities.WordListActivity
import ru.guru.englearn2.View.Adapters.AAEWAdapter
import ru.guru.englearn2.ViewModel.AAEWVM
import ru.guru.englearn2.databinding.FragmentAaewBinding

class AAEWFragment : Fragment(R.layout.fragment_aaew) {

    private lateinit var binding: FragmentAaewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAaewBinding.inflate(inflater, container, false)
        val idWord = requireActivity().intent.getIntExtra("idWord", -1)
        val idLesson = requireActivity().intent.getIntExtra("idLesson", 0)
        val rusListAdapter = AAEWAdapter(requireContext(), ArrayList())
        val viewModel = ViewModelProvider(this)[AAEWVM::class.java]
        val word = viewModel.getWord(idWord)

        requireActivity().title = if (idWord == -1) "Создать слово" else "Редактировать слово"

        binding.apply {
            rusList.adapter = rusListAdapter
            rusList.layoutManager = LinearLayoutManager(requireContext())

            editEng.addTextChangedListener {
                if (editEng.text?.length == 0) engInputLayout.error = "Поле не должно быть пустым"
                else engInputLayout.error = null
            }
            editRus.addTextChangedListener { rusInputLayout.error = null }
            editRus.setOnClickListener{rusInputLayout.error = null}

            rusInputLayout.setEndIconOnClickListener {
                if (editRus.text?.length!! < 1) rusInputLayout.error = "Поле пустое"
                else {
                    rusListAdapter.data.add(editRus.text?.toString()!!)
                    rusListAdapter.notifyItemRangeInserted(rusListAdapter.itemCount, rusListAdapter.itemCount + 1)
                    editRus.setText("")
                }
            }

            okBtn.setOnClickListener {
                if (editRus.text?.length!! >= 1) rusListAdapter.data.add(editRus.text?.toString()!!)
                if (editEng.text?.length!! >= 1){
                    if (rusListAdapter.itemCount >= 1){
                        viewModel.saveWord(idLesson, idWord, editEng.text?.toString()!!, rusListAdapter.data, editTra.text?.toString()!!)

                        val intent = Intent(requireContext(), WordListActivity::class.java)
                        intent.putExtra("idLesson", idLesson)
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireContext(), "Добавте перевод слова", Toast.LENGTH_SHORT).show()
                        rusInputLayout.error = "Добавте перевод"
                    }
                } else {
                    Toast.makeText(requireContext(), "Заполните поле: \"Слово на английском\"", Toast.LENGTH_SHORT).show()
                    engInputLayout.error = "Поле пустое"
                }
            }

            if (idWord != -1){

                // ДЕЛАЕМ ИТЕРФЕЙС ПОНЯТНЫМ ЧТО ЭТО РЕДАКТИРОВАНИЕ
                editEng.setText(word!!.eng)
                if(word.tra != null) editTra.setText(word.tra!!.substring(1, word.tra!!.length - 1))
                rusListAdapter.data.addAll(word.rus)
                rusListAdapter.notifyDataSetChanged()
                okBtn.text = "Редактировать"
                requireActivity().title = "Редактор слова"

                // СОХРАНЯЕМ СЛОВО
                okBtn.setOnClickListener {
                    if (editEng.text?.length!! >= 1) {
                        if (rusListAdapter.itemCount >= 1) {
                            viewModel.saveWord(idLesson, idWord, editEng.text?.toString()!!, rusListAdapter.data, editTra.text?.toString()!!)
                            val intent = Intent(requireContext(), WordListActivity::class.java)
                            intent.putExtra("idLesson", idLesson)
                            startActivity(intent)
                        } else {
                            Toast.makeText(requireContext(), "Добавте перевод слова", Toast.LENGTH_SHORT).show()
                            rusInputLayout.error = "Добавте перевод"
                        }
                    } else{
                        Toast.makeText(requireContext(), "Заполните поле: \"Слово на английском\"", Toast.LENGTH_SHORT).show()
                        engInputLayout.error = "Поле пустое"
                    }
                }
            }

            cancel.setOnClickListener {
                val intent = Intent(requireContext(), WordListActivity::class.java)
                intent.putExtra("idLesson", idLesson)
                startActivity(intent)
            }
        }

        return binding.root
    }

}