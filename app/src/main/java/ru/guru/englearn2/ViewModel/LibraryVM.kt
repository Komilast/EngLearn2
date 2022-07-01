package ru.guru.englearn2.ViewModel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.Realm
import io.realm.RealmResults
import ru.guru.englearn.database.LiveRealmResults
import ru.guru.englearn2.Model.Topic

class LibraryVM : ViewModel() {

    private val realm = Realm.getDefaultInstance()

    private var topics: RealmResults<Topic>? = null


    fun getAllTopics(): RealmResults<Topic>? {
        if (topics == null) topics = realm.where(Topic::class.java).findAll()
        return topics
            }

    override fun onCleared() {
        super.onCleared()

    }

}