package ru.guru.englearn2.View.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.guru.englearn.database.LiveRealmObject
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.Model.Word
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Activities.AAEWActivity
import ru.guru.englearn2.View.Activities.EAALActivity
import ru.guru.englearn2.View.Adapters.WordListAdapter
import ru.guru.englearn2.View.Interfaces.OnWordClickListener
import ru.guru.englearn2.View.Interfaces.SetIdLessonForActivity
import ru.guru.englearn2.ViewModel.WordListVM
import ru.guru.englearn2.databinding.FragmentWordlistBinding
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class WordListFragment : Fragment(R.layout.fragment_wordlist), OnWordClickListener, TextToSpeech.OnInitListener {

    private lateinit var binding: FragmentWordlistBinding
    private lateinit var adapter: WordListAdapter
    private lateinit var textToSpeech: TextToSpeech
    private var words: LiveData<RealmList<Word>>? = null
    private var viewModel: WordListVM? = null
    private var idTopic: Int = 0
    private var idLesson: Int = 0
    private var lesson: LiveRealmObject<Lesson>? = null
    private lateinit var setIdLessonForActivity: SetIdLessonForActivity
    private var onSetImageListener: ActivityResultLauncher<Intent>? = null

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = runBlocking{
        binding = FragmentWordlistBinding.inflate(inflater)
        idTopic = requireActivity().intent.getIntExtra("idTopic", 0)
        idLesson = requireActivity().intent.getIntExtra("idLesson", 0)
        textToSpeech = TextToSpeech(requireContext(), this@WordListFragment)
        adapter = WordListAdapter(requireContext(), ArrayList(), this@WordListFragment)
        onSetImageListener = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == -1){
                binding.lessonImage.setImageDrawable(Drawable.createFromPath(File(File(requireContext().filesDir.path, "images/lessons"), "${lesson!!.value!!.title}.png").path))
            }
        }
        setIdLessonForActivity = requireContext() as SetIdLessonForActivity
        setIdLessonForActivity.set(idLesson)

        binding.apply {

        launch {
            viewModel = ViewModelProvider(this@WordListFragment)[WordListVM::class.java]
            lesson = viewModel!!.getLesson(idLesson)
            words = viewModel!!.getAllWords(idLesson)
            words!!.observe(this@WordListFragment) {
                adapter.setData(ArrayList(words!!.value!!))
            }
            words!!.value!!.addChangeListener{ t -> adapter.setData(ArrayList(t))}
//            File(requireActivity().filesDir.path, "images")

            when {
                idLesson >= 0 -> {
                    lessonImage.setImageDrawable(Drawable.createFromPath(File(File(requireContext().filesDir.path, "images/lessons"), "${lesson!!.value!!.title}.png").path))
                    head.title = lesson!!.value!!.title
                    lesson!!.value!!.addChangeListener( RealmObjectChangeListener<Lesson>{t, changeSet -> head.title = t.title})
                }
                idLesson == -1 -> {
                    head.title = "Избранное"
                }
                idLesson == -2 -> {
                    head.title = "Удалённое"
                }
            }
        }
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireContext())

            if (idLesson < 0) lessonMore.visibility = View.GONE

            lessonMore.setOnClickListener {
                val popupMenu = PopupMenu(requireContext(), lessonMore)
                popupMenu.menuInflater.inflate(R.menu.lesson_menu, popupMenu.menu)
                if (lesson!!.value!!.isFavorite != -1) popupMenu.menu.getItem(1).title = "Удалить из избранного"
                if (idTopic == -2) {
                    popupMenu.menu.getItem(0).title = "Восстановить"
                    popupMenu.menu.getItem(1).isVisible = false
                }
                popupMenu.setOnMenuItemClickListener {
                    when (it.title){
                        "Редактировать урок" -> {
                            val intent = Intent(requireContext(), EAALActivity::class.java)
                            intent.putExtra("idLesson", idLesson)
                            onSetImageListener!!.launch(intent)
                        }
                        "Добавить в избранное", "Удалить из избранного" -> viewModel!!.lessonFav(lesson!!.value!!)
                        "Удалить урок" -> {
                            viewModel!!.deleteLesson(lesson!!.value!!)
                            requireActivity().finish()
                        }
                        "Восстановить" -> {
                            viewModel!!.restoreLesson(lesson!!.value!!)
                            requireActivity().finish()
                        }
                    }
                    return@setOnMenuItemClickListener true
                }
                popupMenu.show()
            }
        }
        Log.d("My", "onCreateView")
        return@runBlocking binding.root
    }


    override fun onMoreClick(view: View, word: Word) {
        val popupMenu = PopupMenu(requireContext(), view)
        when {
            idLesson >= 0 -> popupMenu.menuInflater.inflate(R.menu.wordlist_word_more_menu, popupMenu.menu)
            idLesson == -1 -> popupMenu.menuInflater.inflate(R.menu.wordlist_word_more_menu, popupMenu.menu)
            idLesson == -2 -> popupMenu.menuInflater.inflate(R.menu.del_words_menu, popupMenu.menu)
        }
        if (word.isFavorite != -1 && idLesson != -2) popupMenu.menu.getItem(0).title = "Удалить из избранного"
        popupMenu.setOnMenuItemClickListener {
            when(it.title){
                "Добавить в избранное", "Удалить из избранного" -> viewModel!!.wordFav(word)
                "Удалить" -> viewModel!!.deleteWord(idLesson, word)
                "Восстановить" -> viewModel!!.restoreWord(word)
                "Редактировать" -> {
                    val intent = Intent(requireContext(), AAEWActivity::class.java)
                    intent.putExtra("idWord", word.id)
                    intent.putExtra("idLesson", idLesson)
                    startActivity(intent)
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    override fun onSpeakClick(word: Word) {
        textToSpeech.speak(word.eng, TextToSpeech.QUEUE_ADD, null, "")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            textToSpeech.language = Locale.US
        }
    }

}