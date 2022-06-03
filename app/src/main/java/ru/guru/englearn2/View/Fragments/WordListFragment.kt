package ru.guru.englearn2.View.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.RealmChangeListener
import io.realm.RealmList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.guru.englearn2.Model.Word
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Adapters.WordListAdapter
import ru.guru.englearn2.View.Interfaces.OnWordClickListener
import ru.guru.englearn2.ViewModel.WordListVM
import ru.guru.englearn2.databinding.FragmentWordlistBinding
import java.util.*
import kotlin.collections.ArrayList

class WordListFragment : Fragment(R.layout.fragment_wordlist), OnWordClickListener, TextToSpeech.OnInitListener, RealmChangeListener<RealmList<Word>> {

    private lateinit var binding: FragmentWordlistBinding
    private lateinit var adapter: WordListAdapter
    private lateinit var textToSpeech: TextToSpeech
    private var words: LiveData<RealmList<Word>>? = null
    private var viewModel: WordListVM? = null
    private var idLesson: Int = 0

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = runBlocking{
        binding = FragmentWordlistBinding.inflate(inflater)
        idLesson = requireActivity().intent.getIntExtra("idLesson", 0)
        textToSpeech = TextToSpeech(requireContext(), this@WordListFragment)
        adapter = WordListAdapter(requireContext(), ArrayList(), this@WordListFragment, idLesson)

        launch {
            viewModel = ViewModelProvider(this@WordListFragment)[WordListVM::class.java]
            words = viewModel!!.getAllWords(idLesson)
            words!!.observe(this@WordListFragment) {
                adapter.setData(ArrayList(words!!.value!!))
            }
            words!!.value!!.addChangeListener(this@WordListFragment)
        }

        binding.apply {
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireContext())
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
        if (word.isFavorite && idLesson != -2) popupMenu.menu.getItem(0).title = "Удалить из избранного"
        popupMenu.setOnMenuItemClickListener {
            when(it.title){
                "Добавить в избранное", "Удалить из избранного" -> viewModel!!.wordFav(word)
                "Удалить" -> viewModel!!.deleteWord(idLesson, word)
                "Восстановить" -> viewModel!!.restoreWord(word)
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

    override fun onChange(t: RealmList<Word>) {
        adapter.setData(ArrayList(t))
    }

}