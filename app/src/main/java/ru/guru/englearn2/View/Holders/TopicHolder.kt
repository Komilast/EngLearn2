package ru.guru.englearn2.View.Holders

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.guru.englearn2.Model.Topic
import ru.guru.englearn2.View.Adapters.LessonAdapter
import ru.guru.englearn2.View.Interfaces.OnClickLibrary
import ru.guru.englearn2.View.Interfaces.onLessonClickListener
import ru.guru.englearn2.databinding.ItemTopicBinding

class TopicHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTopicBinding.bind(view)

    fun bind(mTopic: Topic, context: Context, listener: onLessonClickListener, listener2: OnClickLibrary) = with(binding){
        title.text = mTopic.title
        topic.setOnClickListener { listener2.onClickTopic(mTopic) }
        val adapter = LessonAdapter(ArrayList(mTopic.lessons), context, listener, topic.id)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

}