package ru.guru.englearn2.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.where
import ru.guru.englearn.database.LiveRealmObject
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.Model.Menu
import ru.guru.englearn2.Model.Topic

class LessonListVM : ViewModel() {

    private val realm = Realm.getDefaultInstance()

    private var lessonsFav: LiveData<RealmList<Lesson>>? = null
    private var lessons: LiveData<RealmList<Lesson>>? = null

    fun getAllLessons(idTopic: Int): LiveData<RealmList<Lesson>>?{
        return when {
            idTopic >= 0 -> {
                if (lessons == null) lessons = MutableLiveData(realm.where(Topic::class.java).equalTo("id", idTopic).findFirst()!!.lessons)
                lessons
            }
            idTopic == -1 -> {
                if (lessonsFav == null) lessonsFav = MutableLiveData(realm.where(Menu::class.java).findFirst()!!.favLesson)
                lessonsFav
            }
            else -> null
        }
    }


}