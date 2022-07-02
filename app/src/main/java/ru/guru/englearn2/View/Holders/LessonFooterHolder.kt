package ru.guru.englearn2.View.Holders

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.guru.englearn2.View.Activities.EAALActivity
import ru.guru.englearn2.databinding.ItemLessonAddBinding

class LessonFooterHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemLessonAddBinding.bind(view)

    fun bind(context: Context) = with(binding){
        container.setOnClickListener{
            val intent = Intent(context, EAALActivity::class.java)
            context.startActivity(intent)
        }
    }

}