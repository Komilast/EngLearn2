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

    fun getAllWords(idLesson: Int, listener: RealmChangeListener<RealmList<Word>>): LiveData<RealmList<Word>>? {
        when {
            idLesson >= 0 -> {
//                mWords = realm.where(Lesson::class.java).equalTo("id", idLesson).findFirst()!!.words
                if (words == null) {
                    words = MutableLiveData(realm.where(Lesson::class.java).equalTo("id", idLesson).findFirst()!!.words)
                }
                return words
            }
            idLesson == -1 -> {
//                mWordsFav = realm.where(Menu::class.java).findFirst()!!.favWords!!
                if (wordsFav == null) {
                    wordsFav = MutableLiveData(realm.where(Menu::class.java).findFirst()!!.favWords!!)
                }
                return wordsFav
            }
            idLesson == -2 -> {
//                mWordsDel = realm.where(Menu::class.java).findFirst()!!.delWords!!
                if (wordsDel == null) {
                    wordsDel = MutableLiveData(realm.where(Menu::class.java).findFirst()!!.delWords!!)
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