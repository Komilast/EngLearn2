package ru.guru.englearn2.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.Realm
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.Model.Menu
import ru.guru.englearn2.Model.Word

class WordListVM : ViewModel() {

    private val realm = Realm.getDefaultInstance()

    private var words: LiveData<ArrayList<Word>>? = null
    private var wordsFav: LiveData<ArrayList<Word>>? = null

    fun getAllWords(idLesson: Int): LiveData<ArrayList<Word>>? {
        if (idLesson >= 0) {
            if (words == null) {
                words = MutableLiveData(
                    ArrayList(
                        realm.where(Lesson::class.java)
                            .equalTo("id", idLesson)
                            .findFirst()!!
                            .words
                    )
                )
            }
            return words
        }
            else {
                if (wordsFav == null) {
                    wordsFav = MutableLiveData(
                        ArrayList(
                            realm.where(Menu::class.java)
                                .findFirst()!!
                                .favWords!!
                        )
                    )
                }
                return wordsFav
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

    override fun onCleared() {
        super.onCleared()
        realm.close()
    }

}