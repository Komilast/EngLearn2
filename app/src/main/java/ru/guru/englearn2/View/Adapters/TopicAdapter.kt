package ru.guru.englearn2.View.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.guru.englearn2.Model.Topic
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Holders.TopicHolder
import ru.guru.englearn2.View.Interfaces.onLessonClickListener

class TopicAdapter(
        private val context: Context,
        private var data: ArrayList<Topic>,
        private val listener: onLessonClickListener)
    : RecyclerView.Adapter<TopicHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicHolder {
            return TopicHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.item_topic, parent, false))
        }

        override fun onBindViewHolder(holder: TopicHolder, position: Int) {
            holder.bind(data[position], context, listener)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        @SuppressLint("NotifyDataSetChanged")
        fun setData(data: ArrayList<Topic>){
            this.data = data
            notifyDataSetChanged()
        }
}