package ru.guru.englearn2.ViewModel

import androidx.lifecycle.ViewModel
import io.realm.Realm
import io.realm.RealmList
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.Model.Topic

class EAALVM : ViewModel() {

    private val realm = Realm.getDefaultInstance()

    fun getLesson(idLesson: Int): Lesson?{
        return if (idLesson != -1) realm.where(Lesson::class.java).equalTo("id", idLesson).findFirst()!!
        else null
    }

    fun saveLesson(idLesson: Int, idTopic: Int, title: String): Lesson{
        var lesson = Lesson()
        realm.executeTransaction {
            if (idLesson != -1){
                lesson = it.where(Lesson::class.java).equalTo("id", idLesson).findFirst()!!
                lesson.title = title
            } else{
                val topic = it.where(Topic::class.java).equalTo("id", idTopic).findFirst()!!
                val id = if (it.where(Lesson::class.java).max("id") == null) 0
                else it.where(Lesson::class.java).max("id")!!.toInt() + 1
                lesson = it.createObject(Lesson::class.java, id)
                lesson.number = if (topic.lessons.max("number") == null) 0
                else topic.lessons.max("number")!!.toInt() + 1
                lesson.topic = topic
                lesson.title = title
                lesson.isFavorite = -1
                lesson.words = RealmList()
                topic.lessons.add(lesson)
            }
        }
        return lesson
    }

}