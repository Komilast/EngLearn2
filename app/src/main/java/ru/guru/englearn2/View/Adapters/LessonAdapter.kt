package ru.guru.englearn2.View.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Holders.LessonHolder
import ru.guru.englearn2.View.Interfaces.onLessonClickListener

class LessonAdapter(
    private var data: ArrayList<Lesson>,
    private val context: Context,
    private val listener: onLessonClickListener
) : RecyclerView.Adapter<LessonHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonHolder {
        return LessonHolder(LayoutInflater
            .from(context)
            .inflate(R.layout.item_lesson, parent, false))
    }

    override fun onBindViewHolder(holder: LessonHolder, position: Int) {
        holder.bind(data[position], context, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}