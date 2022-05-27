package ru.guru.englearn2.View.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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

class WordListFragment : Fragment(R.layout.fragment_wordlist), OnWordClickListener, TextToSpeech.OnInitListener {

    private lateinit var binding: FragmentWordlistBinding
    private lateinit var adapter: WordListAdapter
    private lateinit var textToSpeech: TextToSpeech
    private var words: LiveData<ArrayList<Word>>? = null
    private var viewModel: WordListVM? = null
    private var idLesson: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idLesson = it.getInt("idLesson", 0)
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = runBlocking{
        binding = FragmentWordlistBinding.inflate(inflater)
        textToSpeech = TextToSpeech(requireContext(), this@WordListFragment)
        adapter = WordListAdapter(requireContext(), ArrayList(), this@WordListFragment)

        launch {
            viewModel = ViewModelProvider(this@WordListFragment)[WordListVM::class.java]
            words = viewModel!!.getAllWordsByLesson(idLesson)
            words!!.observe(this@WordListFragment){
                adapter.setData(words!!.value!!)
            }
        }

        binding.apply {
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireContext())
        }

        return@runBlocking binding.root
    }

    override fun onMoreClick(view: View, word: Word) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.wordlist_word_more_menu, popupMenu.menu)
        if (word.isFavorite) popupMenu.menu.getItem(0).title = "Удалить из избранного"

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