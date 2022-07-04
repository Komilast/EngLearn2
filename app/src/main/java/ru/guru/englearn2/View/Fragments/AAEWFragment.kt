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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAaewBinding.inflate(inflater, container, false)
        val idWord = requireActivity().intent.getIntExtra("idWord", -1)              // ПЕРЕМЕННАЯ ДЛЯ ИДЕНТИФИКАТОРА СЛОВА (ПО УМОЛЧАНИЮ СЛОВО СОЗДАЁТСЯ)
        val idLesson = requireActivity().intent.getIntExtra("idLesson", 0)           // ПЕРЕМЕННАЯ ДЛЯ ИДЕНТИФИКАТОРА УРОКА
        val rusListAdapter = AAEWAdapter(requireContext(), ArrayList())                              // АДАПТЕР ДЛЯ СПИСКА РУСКИХ СЛОВ (ПОКА ЧТО В НЁМ НЕТ ДАННЫХ)
        val viewModel = ViewModelProvider(this)[AAEWVM::class.java]
        val word = viewModel.getWord(idWord)                                                         // САМО СЛОВО (ЕСЛИ СЛОВО СОЗДАЁТСЯ, ТО ЕСТЬ ЕГО ИДЕНТИФИКАТОР РАВЕН -1, ТО ВОЗРАЩАЕТСЯ null

        requireActivity().title = if (idWord == -1) "Создать слово" else "Редактировать слово"       // МЕНЯЕМ ЗАГОЛОВОК АКТИВИТИ В ЗАВИСИМОСТИ ОТ ПОТРЕБНОСТИ

        binding.apply {
            rusList.adapter = rusListAdapter                                                         // ПРИСВАИВАЕМ АДАПТЕР ДЛЯ СПИСКА РУССКИХ СЛОВ
            rusList.layoutManager = LinearLayoutManager(requireContext())                            // НЕ ЗАБЫВАЕМ ПРО МЕНЕДЖЕР

            editEng.addTextChangedListener {                                                         // ОБРАБАТЫВАЕМ ПРАВИЛЬНОСТЬ ВВОДА ДЛЯ АНГЛИЙСКОГО СЛОВА
                if (editEng.text?.length == 0) engInputLayout.error = "Поле не должно быть пустым"   // ЕСЛИ ПОЛЕ ПУСТОЕ, ПОКАЖЕМ ОБ ЭТОМ СООБЩЕНИЕ
                else engInputLayout.error = null                                                     // В ДРУГОМ СЛУЧАЕ СБРОСИМ ЛЮБОЕ СООБЩЕНИЕ
            }
            editRus.addTextChangedListener { rusInputLayout.error = null }                           // ОБРАБАТЫВАЕМ ПРАВИЛЬНОСТЬ ВВОДА РУССКОГО СЛОВА, ДЛЯ ЭТОГО СБРАСЫВАЕМ ВСЕ ОШИБКИ ПРИ ВВОДЕ
            editRus.setOnClickListener{rusInputLayout.error = null}                                  // ПРИ НАЖАТИИ НА ТЕКСТОВОЕ ПОЛЕ РУССКОГО СЛОВА ТОЖЕ СБРОСИМ ОШИБКИ

            rusInputLayout.setEndIconOnClickListener {                                               // ОБРАБАТЫВАМ СОБЫТИЕ ПРИ НАЖАТИИ НА ИКОНКУ ДОБАВЛЕНИЯ СЛОВА В СПИСОК
                if (editRus.text?.length!! < 1) rusInputLayout.error = "Поле пустое"                 // ЕСЛИ ПОЛЕ ПУСТОЕ УКАЗЫВАЕМ НА ЭТО В СООБЩЕНИИ
                else {
                    rusListAdapter.data.add(editRus.text?.toString()!!)                              // ЕСЛИ ПОЛЕ НЕ ПУСТОЕ, ТО ДОБАВЛЯЕМ СЛОВО В МАССИВ АДАПТЕРА СПИСКА РУССКИХ СЛОВ
                    rusListAdapter.notifyItemRangeInserted(                                          // СРАЗУ УВЕДОМЛЯЕМ АДАПТЕР ОБ ИЗМЕНЕНИЯХ ЧТОБЫ СПИСОК ОБНОВИЛСЯ
                        rusListAdapter.itemCount,                                                    // УКАЖЕМ АДАПТЕРУ ПОЗИЦИЮ В КОТОРУЮ ДОБАВИЛИ НОВОЕ СЛОВО
                        rusListAdapter.itemCount + 1                                        // УКАЖЕМ АДАПТЕРУ СКОЛЬКО ТЕПЕРЬ СЛОВ ХОТИМ ВИДЕТЬ
                    )
                    editRus.setText("")                                                              // ОЧИЩАЕМ ПОЛЕ ДЛЯ ВВОДА ЧТОБЫ ПОЛЬЗОВАТЕЛЬ ПОНЯЛ ЧТО СЛОВО ДОБАВИЛОСЬ И ЧТО МОЖНО ВВЕСТИ НОВОЕ СЛОВО ДЛЯ ДОБАВЛЕНИЯ
                }
            }

            okBtn.setOnClickListener {                                                               // ОБРАБАТЫВАЕМ СОХРАНЕНИЕ СЛОВА ПРИ НАЖАТИИ КНОПКИ
                if (editRus.text?.length!! >= 1) rusListAdapter.data.add(editRus.text?.toString()!!) // ЕСЛИ ПОЛЬЗОВАТЕЛЬ ВВЁЛ РУССКОЕ СЛОВО НО ЗАБЫЛ ДОБАВИТЬ ЕГО В СПИСОК, ТО ДЕЛАЕМ ЭТО ЗА НЕГО ПРИ УСЛОВИИ ЧТО ПОЛЕ НЕ ПУСТОЕ
                if (editEng.text?.length!! >= 1){                                                    // ПРОВЕРЯЕМ ЧТО ПОЛЕ АНГЛИЙСКОГО СЛОВА НЕ ПУСТОЕ
                    if (rusListAdapter.itemCount >= 1){                                              // ПРОВЕРЯЕМ ЧТО ЕСТЬ ХОТЯБЫ ОДНО СЛОВО В СПИСКЕ РУССКИХ СЛОВ
                        viewModel.saveWord(                                                          // СОХРАНЯЕМ СЛОВО ЧЕРЕЗ VIEWMODEL
                            idLesson,                                                                // УКАЗЫВАЕМ В КАКОМ УРОКЕ СОХРАНИТЬ СЛОВО
                            idWord,                                                                  // УКАЗЫВАЕМ ID СЛОВА (ЕСЛИ ID = -1, ТО АВТОМАТОМ ПРИСВОИТСЯ НОВЫЙ ID)
                            editEng.text?.toString()!!,                                              // УКАЗЫВАЕМ СЛОВО НА АНГЛИЙСКОМ
                            rusListAdapter.data,                                                     // УКАЗЫВАЕМ СПИСОК РУССКИХ СЛОВ ПЕРЕДАВ МАССИВ ИЗ АДАПТЕРА
                            editTra.text?.toString()!!                                               // УКАЗЫВАЕМ ТРАНСКРИПЦИЮ
                        )

                        val intent = Intent(requireContext(), WordListActivity::class.java)          // ЗАПУСКАЕМ WORDLISTACTIVITY ДЛЯ ВОЗВРАТА ОБРАТНО (МОЖНО ПОДУМАТЬ О ДРУГОМ СПОСОБЕ ВЕРНУТЬСЯ ОБРАТНО)
                        intent.putExtra("idLesson", idLesson)
                        startActivity(intent)
                    } else {                                                                         // ЕСЛИ В СПИСКЕ РУССКИХ СЛОВ ПУСТО, ТО СЛОВО НЕ СОХРАНЯЕМ И ВЫВОДИМ СООБЩЕНИЕ С ОШИБКОЙ
                        Toast.makeText(
                            requireContext(),
                            "Добавте перевод слова",
                            Toast.LENGTH_SHORT
                        ).show()
                        rusInputLayout.error = "Добавте перевод"
                    }
                } else {                                                                             // ЕСЛИ ПОЛЕ ДЛЯ ВВОДА АНГЛИЙСКОГО СЛОВА ПУСТОЕ, ТО ВЫВОДИМ СООБЩЕНИЕ ОБ ЭТОМ
                    Toast.makeText(
                        requireContext(),
                        "Заполните поле: \"Слово на английском\"",
                        Toast.LENGTH_SHORT
                    ).show()
                    engInputLayout.error = "Поле пустое"
                }
                requireActivity().finish()                                                           // ЗАКРЫВАЕМ АКТИВИТИ (КАК И ПИСАЛОСЬ ВЕШЕ, МОЖНО ПРИДУМАТЬ ДРУГОЙ СПОСОБ)
            }

                /** РЕЖИМ РЕДАКТИРОВАНИЯ */

            if (idWord != -1){                                                                       // ЕСЛИ СЛОВО НЕ ИМЕЕТ ИДЕНТИФИКАТОР -1, ТО ЗНАЧИТ СЛОВО НАДО ОТРЕДАКТИРОВАТЬ

                // ДЕЛАЕМ ИТЕРФЕЙС ПОНЯТНЫМ ЧТО ЭТО РЕДАКТИРОВАНИЕ
                editEng.setText(word!!.eng)                                                          // ВПИСЫВАЕМ АНГЛИЙСКОЕ СЛОВО
                if(word.tra != null) editTra.setText(word.tra!!.substring(1, word.tra!!.length - 1)) // ЕСЛИ ЕСТЬ ТРАНСКРИПЦИЯ, ТО НАМ НУЖНО ЕЁ ВПИСАТЬ ПРЕДВАРИТЕЛЬНО УДАЛИВ КВАДРАТНЫЕ СКОБКИ
                rusListAdapter.data.addAll(word.rus)                                                 // ДОБАВЛЯЕМ В СПИСОК РУССКИХ СЛОВ АДАПТЕРА ВСЕ СЛОВА
                rusListAdapter.notifyDataSetChanged()                                                // УВЕДОМЛЯЕМ ОБ ЭТОМ АДАПТЕР
                okBtn.text = "Редактировать"                                                         // ИЗМЕНЯЕМ КНОПКУ "ДОБАВИТЬ" НА "РЕДАКТИРОВАТЬ"
                requireActivity().title = "Редактор слова"                                           // ИЗМЕНЯЕМ ЗАГОЛОВОК АКТИВИТИ НА "РЕДАКТОР СЛОВА"

                // СОХРАНЯЕМ СЛОВО
                okBtn.setOnClickListener {                                                           // ОБРАБАТЫВАЕМ СОБЫТИЯ ПРИ НАЖАТИИ НА КНОПКУ СОХРАНЕНИЯ
                    if (editRus.text?.length!! >= 1)
                        rusListAdapter.data.add(editRus.text?.toString()!!)                          // ЕСЛИ ПОЛЬЗОВАТЕЛЬ ВВЁЛ РУССКОЕ СЛОВО НО ЗАБЫЛ ДОБАВИТЬ ЕГО В СПИСОК, ТО ДЕЛАЕМ ЭТО ЗА НЕГО ПРИ УСЛОВИИ ЧТО ПОЛЕ НЕ ПУСТОЕ
                    if (editEng.text?.length!! >= 1) {                                               // ПРОВЕРЯЕМ ЧТО ПОЛЕ АНГЛИЙСКОГО СЛОВА НЕ ПУСТОЕ
                        if (rusListAdapter.itemCount >= 1) {                                         // ПРОВЕРЯЕМ ЧТО ЕСТЬ ХОТЯБЫ ОДНО СЛОВО В СПИСКЕ РУССКИХ СЛОВ
                            viewModel.saveWord(                                                      // СОХРАНЯЕМ СЛОВО ЧЕРЕЗ VIEWMODEL
                                idLesson,                                                            // УКАЗЫВАЕМ В КАКОМ УРОКЕ СОХРАНИТЬ СЛОВО
                                idWord,                                                              // УКАЗЫВАЕМ ID СЛОВА (ЕСЛИ ID = -1, ТО АВТОМАТОМ ПРИСВОИТСЯ НОВЫЙ ID)
                                editEng.text?.toString()!!,                                          // УКАЗЫВАЕМ СЛОВО НА АНГЛИЙСКОМ
                                rusListAdapter.data,                                                 // УКАЗЫВАЕМ СПИСОК РУССКИХ СЛОВ ПЕРЕДАВ МАССИВ ИЗ АДАПТЕРА
                                editTra.text?.toString()!!                                           // УКАЗЫВАЕМ ТРАНСКРИПЦИЮ
                            )
                            val intent = Intent(requireContext(), WordListActivity::class.java)      // ЗАПУСКАЕМ WORDLISTACTIVITY ДЛЯ ВОЗВРАТА ОБРАТНО (МОЖНО ПОДУМАТЬ О ДРУГОМ СПОСОБЕ ВЕРНУТЬСЯ ОБРАТНО)
                            intent.putExtra("idLesson", idLesson)
                            startActivity(intent)
                        } else {                                                                     // ЕСЛИ В СПИСКЕ РУССКИХ СЛОВ ПУСТО, ТО СЛОВО НЕ СОХРАНЯЕМ И ВЫВОДИМ СООБЩЕНИЕ С ОШИБКОЙ
                            Toast.makeText(
                                requireContext(),
                                "Добавте перевод слова",
                                Toast.LENGTH_SHORT
                            ).show()
                            rusInputLayout.error = "Добавте перевод"
                        }
                    } else{                                                                          // ЕСЛИ ПОЛЕ ДЛЯ ВВОДА АНГЛИЙСКОГО СЛОВА ПУСТОЕ, ТО ВЫВОДИМ СООБЩЕНИЕ ОБ ЭТОМ
                        Toast.makeText(
                            requireContext(),
                            "Заполните поле: \"Слово на английском\"",
                            Toast.LENGTH_SHORT
                        ).show()
                        engInputLayout.error = "Поле пустое"
                    }
                    requireActivity().finish()                                                       // ЗАКРЫВАЕМ АКТИВИТИ (КАК И ПИСАЛОСЬ ВЕШЕ, МОЖНО ПРИДУМАТЬ ДРУГОЙ СПОСОБ)
                }
            }

            cancel.setOnClickListener {                                                              // ПРОСТО ЗАКРЫВАЕМ АКТИВИТИ ЕСЛИ НАЖАТА КНОПКА CANCEL
                requireActivity().finish()
            }
        }

        return binding.root
    }

}