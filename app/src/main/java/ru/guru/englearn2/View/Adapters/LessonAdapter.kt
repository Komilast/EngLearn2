package ru.guru.englearn2.View.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Holders.LessonFooterHolder
import ru.guru.englearn2.View.Holders.LessonHolder
import ru.guru.englearn2.View.Interfaces.onLessonClickListener

class LessonAdapter(
    var data: ArrayList<Lesson>,
    private val context: Context,
    private val listener: onLessonClickListener,
    private val idTopic: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object{
        private const val TYPE_NORMAL = 0
        private const val TYPE_FOOTER = 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == data.size) return TYPE_FOOTER
        return TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_NORMAL) {
            LessonHolder(
                LayoutInflater
                    .from(context)
                    .inflate(R.layout.item_lesson, parent, false)
            )
        } else {
            LessonFooterHolder(
                LayoutInflater
                    .from(context)
                    .inflate(R.layout.item_lesson_add, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LessonHolder) holder.bind(data[position], context, listener)
        else if (holder is LessonFooterHolder) holder.bind(listener, idTopic)
    }

    override fun getItemCount(): Int {
        return if (idTopic >= 0) data.size + 1 else data.size
    }


}