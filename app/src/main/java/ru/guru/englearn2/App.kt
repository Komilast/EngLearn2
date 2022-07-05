package ru.guru.englearn2

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import java.io.File
import java.io.FileOutputStream

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        /** Инициализация и настройка базы данных Realm */

        Realm.init(this)
        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .assetFile("default.realm")
                .schemaVersion(1)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build()
        )

        /** Копирование всех картинок из assets в корневой каталог приложения */

        // Создаём переменную для проверки первоначального запуска программы
        val pref = getSharedPreferences("AppStatus", MODE_PRIVATE)
        val check = pref.getInt("NewApp", 0)

        // Запуск алгоритма если приложение запускается впервые
        if (check == 0) {

            // Получаем название всех файлов из assets/images
            val images: ArrayList<String> = ArrayList()
            images.addAll(assets.list("images/")!!)

            // Удаляем из списка все лишние системные файлы
            images.removeAt(0)
            images.removeAt(0)
            images.removeAt(0)

            // Получаем директорию файлов
            val path = filesDir.path

            // Создаём новую папку для картинок
            val imagesDir = File(path, "images")
            imagesDir.mkdirs()

            // Создаём цикл для копирования новых картинок
            for (image in images) {
                val file = File(imagesDir, image)
                    file.createNewFile()
                    FileOutputStream(file).use {
                        it.write(assets.open("images/$image").readBytes())
                    }
            }
            // Записываем что приложение запустилось впервые
            val editor = pref.edit()
            editor.putInt("NewApp", 1)
            editor.apply()
        }
    }

}