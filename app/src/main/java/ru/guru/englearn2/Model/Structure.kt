package ru.guru.englearn2.Model

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
    var temp: String? = null
): RealmObject()