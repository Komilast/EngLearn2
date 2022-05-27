package ru.guru.englearn2.View.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.guru.englearn2.Model.Word
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Holders.WordListHolder
import ru.guru.englearn2.View.Interfaces.OnWordClickListener

class WordListAdapter(
    private val context: Context,
    private var data: ArrayList<Word>,
    private val listener: OnWordClickListener
) : RecyclerView.Adapter<WordListHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordListHolder {
        return WordListHolder(LayoutInflater.from(context).inflate(R.layout.item_word, parent, false))
    }

    override fun onBindViewHolder(holder: WordListHolder, position: Int) {
        holder.bind(context, listener, data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<Word>){
        this.data = data
        notifyDataSetChanged()
    }


}