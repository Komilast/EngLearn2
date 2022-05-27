package ru.guru.englearn2.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.Realm
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.Model.Word

class WordListVM : ViewModel() {

    private val realm = Realm.getDefaultInstance()

    private var words: LiveData<ArrayList<Word>>? = null

    fun getAllWordsByLesson(idLesson: Int): LiveData<ArrayList<Word>>?{
        if (words == null) words = MutableLiveData(ArrayList(realm.where(Lesson::class.java)
            .equalTo("id", idLesson)
            .findFirst()!!
            .words))
        return words
    }

    override fun onCleared() {
        super.onCleared()
        realm.close()
    }

}