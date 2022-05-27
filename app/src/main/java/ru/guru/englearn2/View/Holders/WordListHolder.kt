package ru.guru.englearn2.View.Holders

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.guru.englearn2.Model.Word
import ru.guru.englearn2.View.Interfaces.OnWordClickListener
import ru.guru.englearn2.databinding.ItemWordBinding

class WordListHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemWordBinding.bind(view)

    fun bind(context: Context, listener: OnWordClickListener, word: Word) = with(binding){
        eng.text = word.eng
        rus.text = word.rus.joinToString("; ")
        tra.text = word.tra
        speak.setOnClickListener{listener.onSpeakClick(word)}
        more.setOnClickListener{listener.onMoreClick(more, word)}
    }

}