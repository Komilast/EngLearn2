package ru.guru.exceltorealm

import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.*

open class Topic(
    @Required
    var title: String = "",
    @PrimaryKey
    var id: Int? = null,
    var lessons: RealmList<Lesson> = RealmList()
): RealmObject()

open class Lesson(
    var title: String = "",
    @PrimaryKey
    var id: Int? = null,
    var number: Int = 0,
    var topic: Topic? = null,
    var isFavorite: Int = -1,
    var words: RealmList<Word> = RealmList()
): RealmObject()

open class Word(
    @Index
    var eng: String = "",
    var rus: RealmList<String> = RealmList(),
    var tra: String? = null,
    var lesson: Lesson? = null,
    @PrimaryKey
    var id: Int? = null,
    var number: Int = 0,
    @Ignore
    var type: String? = null,
    var links: RealmList<Word>? = null,
    var temp: String? = null,
    var isFavorite: Int = -1
): RealmObject()

open class Menu(
    var favWords: RealmList<Word>? = null,
    var favLesson: RealmList<Lesson>? = null,
    var delWords: RealmList<Word>? = null,
    var delLesson: RealmList<Lesson>? = null
) : RealmObject()