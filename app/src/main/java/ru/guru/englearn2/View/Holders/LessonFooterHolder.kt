package ru.guru.englearn2.View.Holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.guru.englearn2.View.Interfaces.onLessonClickListener
import ru.guru.englearn2.databinding.ItemLessonAddBinding

class LessonFooterHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemLessonAddBinding.bind(view)

    fun bind(listener: onLessonClickListener, idTopic: Int) = with(binding){
        container.setOnClickListener{
            listener.onAddClick(idTopic)
        }
    }

}