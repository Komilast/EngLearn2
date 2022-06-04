package ru.guru.englearn2.ViewModel

import androidx.lifecycle.ViewModel
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.where
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.Model.Word

class AAEWVM : ViewModel() {

private val realm = Realm.getDefaultInstance()

    fun saveWord(idLesson: Int, idWord: Int, eng: String, rus: ArrayList<String>, tra: String){
        var word: Word
        if (idWord == -1){
            realm.executeTransaction {
                val lesson = it.where(Lesson::class.java).equalTo("id", idLesson).findFirst()!!
                val nextId = realm.where(Word::class.java).max("id")!!.toInt() + 1
                word = realm.createObject(Word::class.java, nextId)
                word.lesson = lesson
                word.number = lesson.words.max("id")!!.toInt()
                word.eng = eng
                word.rus.addAll(rus)
                word.tra = "[$tra]"
                lesson.words.add(word)
            }
        }else {
            realm.executeTransaction{
                word = it.where(Word::class.java).equalTo("id", idWord).findFirst()!!
                word.eng = eng
                word.rus.clear()
                word.rus.addAll(rus)
                word.tra = "[$tra]"
            }
        }
    }

    fun getWord(idWord: Int): Word?{
        return if (idWord != -1) realm.where(Word::class.java).equalTo("id", idWord).findFirst()!!
        else null
    }

    override fun onCleared() {
        super.onCleared()
        realm.close()
    }

}