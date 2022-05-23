package ru.guru.englearn2

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .assetFile("default.realm")
                .schemaVersion(1)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build()
        )
    }

}