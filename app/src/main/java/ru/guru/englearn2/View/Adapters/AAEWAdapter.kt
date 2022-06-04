package ru.guru.englearn2.View.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Holders.AAEWHolder

class AAEWAdapter(private val context: Context, var data: ArrayList<String>) : RecyclerView.Adapter<AAEWHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AAEWHolder {
        return AAEWHolder(LayoutInflater.from(context).inflate(R.layout.item_aaew_word_rus, parent, false))
    }

    override fun onBindViewHolder(holder: AAEWHolder, position: Int) {
        holder.text.text = data[position]
        holder.word.setOnClickListener{
            data.removeAt(position)
            notifyItemRangeRemoved(position, itemCount)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}