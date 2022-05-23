package ru.guru.exceltorealm

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration
            .Builder()
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .schemaVersion(1)
            .build())

    }

}