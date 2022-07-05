package ru.guru.englearn2.View.Interfaces

import ru.guru.englearn2.Model.Lesson

interface onLessonClickListener {
    fun onClick(lesson: Lesson)
    fun onAddClick(idTopic: Int)
}