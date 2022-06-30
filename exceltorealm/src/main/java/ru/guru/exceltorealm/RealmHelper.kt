package ru.guru.exceltorealm

import android.content.Context
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.createObject
import io.realm.kotlin.executeTransactionAwait
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope

class RealmHelper(private val context: Context) {



    @Suppress("NAME_SHADOWING")
    suspend fun createBase() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAwait(Dispatchers.IO) { val array = ExcelHelper().getBooks(context)
            for (mTopic in array){
                val id = realm.where(Topic::class.java).max("id")
                val nextId = if (id == null) 0 else id.toInt() + 1
                val topic = realm.createObject(Topic::class.java, nextId)
                topic.title = mTopic.title
                for (mLesson in mTopic.sheets){
                    val id = realm.where(Lesson::class.java).max("id")
                    val nextId = if (id == null) 0 else id.toInt() + 1
                    val lesson = realm.createObject(Lesson::class.java, nextId)
                    topic.lessons.add(lesson)
                    lesson.title = mLesson.title
                    lesson.number = topic.lessons.indexOf(lesson)
                    lesson.topic = topic
                    lesson.isFavorite = -1
                    for (mWord in mLesson.rows){
                        val id = realm.where(Word::class.java).max("id")
                        val nextId = if (id == null) 0 else id.toInt() + 1
                        val word = realm.createObject(Word::class.java, nextId)
                        lesson.words.add(word)
                        word.eng = mWord.cells[0]
                        word.rus.addAll(mWord.cells[1].split("; "))
                        if (mWord.cells[2] != "-") word.tra = mWord.cells[2]
                        if (mWord.cells[3] != "-") word.type = mWord.cells[3]
                        if (mWord.cells[4] != "-") word.temp = mWord.cells[4]
                        word.number = lesson.words.indexOf(word)
                        word.lesson = lesson
                        word.isFavorite = -1
                    }
                }
            }
            val words = it.where(Word::class.java).findAll()
            for (word in words){
                if (word.temp != null) for (link in word.temp!!.split("; ")){
                    val linkWord = it.where(Word::class.java).equalTo("eng", link).findFirst()
                    if (word.links == null) word.links = RealmList()
                    word.links!!.add(linkWord)
                    word.temp = null
                    }
            }
            val menu = it.createObject(Menu::class.java)
            menu.favWords = RealmList()
            menu.delWords = RealmList()
            menu.favWords = RealmList()
        }

    }

}