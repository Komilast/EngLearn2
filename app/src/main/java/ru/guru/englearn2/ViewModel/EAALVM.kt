package ru.guru.englearn2.ViewModel

import androidx.lifecycle.ViewModel
import io.realm.Realm
import io.realm.kotlin.where
import ru.guru.englearn2.Model.Lesson

class EAALVM : ViewModel() {

    private val realm = Realm.getDefaultInstance()

    fun getLesson(idLesson: Int): Lesson{
        return realm.where(Lesson::class.java).equalTo("id", idLesson).findFirst()!!
    }

    fun saveLesson(idLesson: Int, title: String){
        realm.executeTransaction {
            realm.where(Lesson::class.java).equalTo("id", idLesson).findFirst()!!.title = title
        }
    }

}