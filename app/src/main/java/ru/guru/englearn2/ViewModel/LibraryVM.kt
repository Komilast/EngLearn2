package ru.guru.englearn2.ViewModel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.Realm
import ru.guru.englearn.database.LiveRealmResults
import ru.guru.englearn2.Model.Topic

class LibraryVM : ViewModel() {

    private val realm = Realm.getDefaultInstance()

    private var topics: LiveRealmResults<Topic>? = null


    fun getAllTopics(): LiveRealmResults<Topic>? {
        if (topics == null) topics = LiveRealmResults(realm.where(Topic::class.java).findAll())
        return topics
            }

    override fun onCleared() {
        super.onCleared()

    }

}