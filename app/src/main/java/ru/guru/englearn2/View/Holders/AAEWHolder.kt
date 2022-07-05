package ru.guru.englearn2.View.Holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.guru.englearn2.databinding.ItemAaewWordRusBinding

class AAEWHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemAaewWordRusBinding.bind(view)
    val word = binding.word
    val text = binding.text

}