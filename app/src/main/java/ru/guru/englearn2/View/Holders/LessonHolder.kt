package ru.guru.englearn2.View.Holders

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.View.Interfaces.onLessonClickListener
import ru.guru.englearn2.databinding.ItemLessonBinding

class LessonHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemLessonBinding.bind(view)

    fun bind(lesson: Lesson, context: Context, listener: onLessonClickListener) = with(binding){
        title.text = lesson.title
        image.setImageDrawable(Drawable.createFromStream(context.assets.open("images/" + lesson.title + ".png"), lesson.title))
    }

}