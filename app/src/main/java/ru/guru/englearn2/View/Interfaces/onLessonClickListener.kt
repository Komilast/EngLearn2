package ru.guru.englearn2.View.Interfaces

import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.Model.Topic

interface onLessonClickListener {
    fun onClick(lesson: Lesson)
    fun onAddClick(idTopic: Int)
}