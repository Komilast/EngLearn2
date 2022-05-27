package ru.guru.englearn2.View.Interfaces

import android.view.View
import ru.guru.englearn2.Model.Word

interface OnWordClickListener {

    fun onMoreClick(view: View, word: Word)

    fun onSpeakClick(word: Word)

}