package ru.altmanea.webapp.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class Schedule(
    val name: String,
    val typeoflesson: String,
    val time: String,
    val dayofweek: String,
    val typeofweek: String,
    val group: String,
    val teacher: String,
    val audience: Int,
    val _id: String
)

val Schedule.json
    get() = Json.encodeToString(this)

enum class LessonType (val mark: String){
    LECTURE("Лекция"),
    LABORATORY("Лабораторная"),
    PRACTICE("Практика"),
    KSR("КСР"),
    KRB("КРБ");

    companion object {
        val list = listOf(LECTURE, LABORATORY, PRACTICE, KSR, KRB)
    }
}

enum class DayOfWeek (val mark: String){
    MON("Понедельник"),
    TUE("Вторник"),
    WEN("Среда"),
    THU("Четверг"),
    FRI("Пятница"),
    SAT("Суббота");

    companion object {
        val list = listOf(MON, TUE, WEN, THU, FRI, SAT)
    }
}

enum class Number (val mark: String){
    FIRST("08:00-09:30"),
    SECOND("09:45-11:15"),
    THIRD("11:30-13:00"),
    FOURTH("13:55-15:25"),
    FIFTH("15:40-17:10");

    companion object {
        val list = listOf(FIRST, SECOND, THIRD, FOURTH, FIFTH)
    }
}

enum class TypeOfWeek (val mark: String){
    ODD("Нечетная"),
    EVEN("Четная");

    companion object {
        val list = listOf(ODD, EVEN)
    }
}