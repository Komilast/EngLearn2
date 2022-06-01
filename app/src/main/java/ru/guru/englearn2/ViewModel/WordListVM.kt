package ru.guru.englearn2.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmList
import io.realm.kotlin.where
import ru.guru.englearn.database.LiveRealmObject
import ru.guru.englearn.database.LiveRealmResults
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.Model.Menu
import ru.guru.englearn2.Model.Word

class WordListVM : ViewModel() {

    private val realm = Realm.getDefaultInstance()

    private var words: LiveData<ArrayList<Word>>? = null
    private var wordsFav: LiveData<ArrayList<Word>>? = null
    private var wordsDel: LiveData<ArrayList<Word>>? = null

    fun getAllWords(idLesson: Int, listener: RealmChangeListener<RealmList<Word>>): LiveData<ArrayList<Word>>? {
        when {
            idLesson >= 0 -> {
                val wordList = realm.where(Lesson::class.java).equalTo("id", idLesson).findFirst()!!.words
                wordList.addChangeListener(listener)
                if (words == null) {
                    words = MutableLiveData(ArrayList(wordList))
                }
                return words
            }
            idLesson == -1 -> {
                val wordList = realm.where(Menu::class.java).findFirst()!!.favWords!!
                wordList.addChangeListener(listener)
                if (wordsFav == null) {
                    wordsFav = MutableLiveData(ArrayList(wordList))
                }
                return wordsFav
            }
            idLesson == -2 -> {
                val wordList = realm.where(Menu::class.java).findFirst()!!.delWords!!
                wordList.addChangeListener(listener)
                if (wordsDel == null) {
                    wordsDel = MutableLiveData(ArrayList(wordList))
                }
                return wordsDel
            }
            else -> {return null}
        }
    }

    fun wordFav(word: Word) {
        realm.executeTransaction {
            val favWords = it.where(Menu::class.java).findFirst()!!.favWords!!
            if (!word.isFavorite) {
                word.isFavorite = true
                favWords.add(word)

            } else {
                favWords.remove(word)
                word.isFavorite = false
            }
        }
    }

    fun deleteWord(idLesson: Int, word: Word){
        val menu = realm.where(Menu::class.java).findFirst()!!
        val delWords = menu.delWords!!
        if (idLesson >= 0){
            realm.executeTransaction {
                delWords.add(word)
                word.lesson!!.words.remove(word)
                it.where(Menu::class.java).findFirst()!!.favWords!!.remove(word)
            }
        } else{
            realm.executeTransaction{
                delWords.deleteFromRealm(delWords.indexOf(word))
            }
        }
    }

    fun restoreWord(word: Word){
        realm.executeTransaction {
            word.lesson!!.words.add(word.number, word)
            it.where(Menu::class.java).findFirst()!!.delWords!!.remove(word)
            if (word.isFavorite) it.where(Menu::class.java).findFirst()!!.favWords!!.add(word)
        }
    }

    override fun onCleared() {
        super.onCleared()
        realm.close()
    }

}