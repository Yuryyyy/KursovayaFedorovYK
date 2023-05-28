package ru.altmanea.webapp.repo

import org.litote.kmongo.getCollection
import ru.altmanea.webapp.data.*
import ru.altmanea.webapp.data.Number
import ru.altmanea.webapp.mongoDatabase
import java.util.*

val audiencesDb = mongoDatabase.getCollection<Audience>().apply { drop() }
val groupsDb = mongoDatabase.getCollection<Group>().apply { drop() }
val lessonsDb = mongoDatabase.getCollection<Lesson>().apply { drop() }
val schedulesDb = mongoDatabase.getCollection<Schedule>().apply { drop() }
val teachersDb = mongoDatabase.getCollection<Teacher>().apply { drop() }

fun createTestData() {
    listOf(
        Audience(322, UUID.randomUUID().toString()),
        Audience(323, UUID.randomUUID().toString()),
        Audience(324, UUID.randomUUID().toString()),
        Audience(325, UUID.randomUUID().toString()),
        Audience(326, UUID.randomUUID().toString()),
        Audience(327, UUID.randomUUID().toString()),
        Audience(328, UUID.randomUUID().toString()),
        Audience(329, UUID.randomUUID().toString()),
        Audience(330, UUID.randomUUID().toString()),
        Audience(467, UUID.randomUUID().toString()),
        Audience(120, UUID.randomUUID().toString())
    ).apply {
        map {
            audiencesDb.insertOne(it)
        }
    }

    listOf(
        Group("20з", UUID.randomUUID().toString()),
        Group("20м", UUID.randomUUID().toString()),
        Group("20и", UUID.randomUUID().toString())
    ).apply {
        map {
            groupsDb.insertOne(it)
        }
    }

    listOf(
        Teacher("Альтман", "Евгений", "Анатольевич", UUID.randomUUID().toString()),
        Teacher("Елизаров", "Дмитрий", "Александрович", UUID.randomUUID().toString()),
        Teacher("Малютин", "Андрей", "Геннадьевич", UUID.randomUUID().toString()),
        Teacher("Каштанов","Алексей","Леонидович", UUID.randomUUID().toString()),
        Teacher("Кладов", "Эдуард", "Владимирович", UUID.randomUUID().toString()),
        Teacher("Окишев", "Андрей", "Сергеевич", UUID.randomUUID().toString())
    ).apply {
        map {
            teachersDb.insertOne(it)
        }
    }

    listOf(
        Lesson("Физическая культура", UUID.randomUUID().toString()),
        Lesson("Тестирование программ", UUID.randomUUID().toString()),
        Lesson("Инфокоммуникационные системы и сети", UUID.randomUUID().toString()),
        Lesson("Компьютерные комплексы и сети", UUID.randomUUID().toString()),
        Lesson("Прикладное программирование", UUID.randomUUID().toString())
    ).apply {
        map {
            lessonsDb.insertOne(it)
        }
    }

    val groups = groupsDb.find().toList()
    val teachers = teachersDb.find().toList()
    val audiences = audiencesDb.find().toList()
    val lessons = lessonsDb.find().toList()

    listOf(
        Schedule(
            lessons[0].name,
            LessonType.LECTURE.mark,
            Number.FIRST.mark,
            DayOfWeek.MON.mark,
            TypeOfWeek.ODD.mark,
            groups[1].name,
            teachers[4].fullname(),
            audiences[10].classroom,
            UUID.randomUUID().toString()
        ),
        Schedule(
            lessons[1].name,
            LessonType.LABORATORY.mark,
            Number.SECOND.mark,
            DayOfWeek.MON.mark,
            TypeOfWeek.ODD.mark,
            groups[2].name,
            teachers[1].fullname(),
            audiences[9].classroom,
            UUID.randomUUID().toString()
        ),
        Schedule(
            lessons[2].name,
            LessonType.LABORATORY.mark,
            Number.THIRD.mark,
            DayOfWeek.MON.mark,
            TypeOfWeek.ODD.mark,
            groups[0].name,
            teachers[2].fullname(),
            audiences[3].classroom,
            UUID.randomUUID().toString()
        ),
        Schedule(
            lessons[3].name,
            LessonType.LABORATORY.mark,
            Number.SECOND.mark,
            DayOfWeek.TUE.mark,
            TypeOfWeek.ODD.mark,
            groups[2].name,
            teachers[5].fullname(),
            audiences[3].classroom,
            UUID.randomUUID().toString()
        ),
        Schedule(
            lessons[4].name,
            LessonType.LECTURE.mark,
            Number.FOURTH.mark,
            DayOfWeek.THU.mark,
            TypeOfWeek.ODD.mark,
            groups[2].name,
            teachers[0].fullname(),
            audiences[4].classroom,
            UUID.randomUUID().toString()
        ),
        Schedule(
            lessons[2].name,
            LessonType.KSR.mark,
            Number.SECOND.mark,
            DayOfWeek.SAT.mark,
            TypeOfWeek.ODD.mark,
            groups[1].name,
            teachers[2].fullname(),
            audiences[8].classroom,
            UUID.randomUUID().toString()
        )
    ).apply {
        map {
            schedulesDb.insertOne(it)
        }
    }
}
