package ru.guru.englearn2.View.Holders

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmChangeListener
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.View.Interfaces.onLessonClickListener
import ru.guru.englearn2.databinding.ItemLessonBinding
import java.io.File

class LessonHolder(view: View) : RecyclerView.ViewHolder(view), RealmChangeListener<Lesson> {

    private val binding = ItemLessonBinding.bind(view)
    private val title = binding.title

    fun bind(lesson: Lesson, context: Context, listener: onLessonClickListener) = with(binding){
        lesson.addChangeListener(this@LessonHolder)
        title.text = lesson.title
        image.setImageDrawable(Drawable.createFromPath(File(File(context.filesDir.path, "images"), "${lesson.title}.png").path))
        container.setOnClickListener {
            listener.onClick(lesson)
        }
    }

    override fun onChange(t: Lesson) {
        title.text = t.title
    }

}