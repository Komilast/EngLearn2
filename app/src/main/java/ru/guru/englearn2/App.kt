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

            // Получаем название всех файлов из assets/images/lessons
            val imagesLessons: ArrayList<String> = ArrayList()
            imagesLessons.addAll(assets.list("images/lessons")!!)

            // Удаляем из списка все лишние системные файлы
//            imagesLessons.removeAt(0)
//            imagesLessons.removeAt(0)
//            imagesLessons.removeAt(0)

            // Получаем название всех файлов из assets/images/topics
            val imagesTopics: ArrayList<String> = ArrayList()
            imagesTopics.addAll(assets.list("images/topics")!!)

            // Удаляем из списка все лишние системные файлы
//            imagesTopics.removeAt(0)
//            imagesTopics.removeAt(0)
//            imagesTopics.removeAt(0)

            // Получаем директорию файлов
            val path = filesDir.path

            // Создаём новую папку для картинок
            val imagesDir = File(path, "images")
            imagesDir.mkdirs()

            // Создаём новую папку для картинок уроков
            val imagesLessonsDir = File(imagesDir, "lessons")
            imagesLessonsDir.mkdirs()

            // Создаём новую папку для картинок разделов
            val imagesTopicsDir = File(imagesDir, "topics")
            imagesTopicsDir.mkdirs()

            // Создаём цикл для копирования новых картинок уроков
            for (image in imagesLessons) {
                val file = File(imagesLessonsDir, image)
                    file.createNewFile()
                    FileOutputStream(file).use {
                        it.write(assets.open("images/lessons/$image").readBytes())
                    }
            }

            // Создаём цикл для копирования новых картинок разделов
            for (image in imagesTopics){
                val file = File(imagesTopicsDir, image)
                file.createNewFile()
                FileOutputStream(file).use {
                    it.write(assets.open("images/topics/$image").readBytes())
                }
            }

            // Записываем что приложение запустилось впервые
            val editor = pref.edit()
            editor.putInt("NewApp", 1)
            editor.apply()
        }
    }

}