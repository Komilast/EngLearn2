package ru.guru.englearn2.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmList
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.Model.Menu
import ru.guru.englearn2.Model.Word

class WordListVM : ViewModel() {

    private val realm = Realm.getDefaultInstance()

    private var words: LiveData<RealmList<Word>>? = null
    private var wordsFav: LiveData<RealmList<Word>>? = null
    private var wordsDel: LiveData<RealmList<Word>>? = null

    fun getAllWords(idLesson: Int): LiveData<RealmList<Word>>? {
        when {
            idLesson >= 0 -> {
                if (words == null) {
                    words = MutableLiveData(realm.where(Lesson::class.java).equalTo("id", idLesson).findFirst()!!.words)
                }
                return words
            }
            idLesson == -1 -> {
                if (wordsFav == null) {
                    wordsFav = MutableLiveData(realm.where(Menu::class.java).findFirst()!!.favWords!!)
                }
                return wordsFav
            }
            idLesson == -2 -> {
                if (wordsDel == null) {
                    wordsDel = MutableLiveData(realm.where(Menu::class.java).findFirst()!!.delWords!!)
                }
                return wordsDel
            }
            else -> {
                return null
            }
        }
    }

    fun wordFav(word: Word) {
        realm.executeTransaction {
            val favWords = it.where(Menu::class.java).findFirst()!!.favWords!!
            if (word.isFavorite != -1) {
                word.isFavorite = favWords.max("isFavorite")!!.toInt() + 1
                favWords.add(word)

            } else {
                favWords.remove(word)
                word.isFavorite = -1
            }
        }
    }

    fun deleteWord(idLesson: Int, word: Word) {
        val menu = realm.where(Menu::class.java).findFirst()!!
        val delWords = menu.delWords!!
        realm.executeTransaction {
            when {
                idLesson >= 0 || idLesson == -1 -> {
                    delWords.add(word)
                    word.lesson!!.words.remove(word)
                    it.where(Menu::class.java).findFirst()!!.favWords!!.remove(word)
                }
                idLesson == -2 -> delWords.deleteFromRealm(delWords.indexOf(word))
            }
        }
    }

    fun restoreWord(word: Word) {
        realm.executeTransaction {
            word.lesson!!.words.add(word)
            word.lesson!!.words.sortBy { it.number }
            it.where(Menu::class.java).findFirst()!!.delWords!!.remove(word)
            if (word.isFavorite != -1) it.where(Menu::class.java).findFirst()!!.favWords!!.add(word)
            it.where(Menu::class.java).findFirst()!!.favWords!!.sortBy { it.isFavorite }
        }
    }

    override fun onCleared() {
        super.onCleared()
        realm.close()
    }

}