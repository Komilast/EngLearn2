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

    private var topics: LiveData<ArrayList<Topic>>? = null


    fun getAllTopics(): LiveData<ArrayList<Topic>>? {
        if (topics == null) topics = MutableLiveData(ArrayList(realm.where(Topic::class.java).findAll()))
        return topics
            }

    override fun onCleared() {
        super.onCleared()

    }

}