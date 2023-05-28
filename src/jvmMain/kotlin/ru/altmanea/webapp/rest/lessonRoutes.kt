package ru.altmanea.webapp.rest

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.updateOne
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Lesson
import ru.altmanea.webapp.data.Schedule
import ru.altmanea.webapp.repo.lessonsDb
import ru.altmanea.webapp.repo.schedulesDb
import java.util.*

fun Route.lessonRoutes() {
    route(Config.lessonsPath) {
        get {
            val lessons = lessonsDb.find().toList() as List<Lesson>
            if (lessons.isEmpty())
                return@get call.respondText(
                    "Lessons not found", status = HttpStatusCode.NotFound
                )
            call.respond(lessons)
        }
        post {
            val lesson = call.receive<Lesson>()
            val lessonId = Lesson(lesson.name, UUID.randomUUID().toString())
            if (lessonsDb.find(Lesson::name eq lesson.name).firstOrNull() != null)
                return@post call.respondText(
                    "The lesson already exists", status = HttpStatusCode.BadRequest
                )
            lessonsDb.insertOne(lessonId)
            call.respondText(
                "Lesson stored correctly", status = HttpStatusCode.Created
            )
        }
        delete("{id}") {
            val id = call.parameters["id"]
                ?: return@delete call.respondText(
                    "Missing or malformed id", status = HttpStatusCode.BadRequest
                )
            val lesson = lessonsDb.findOne(Lesson::_id eq id)
            lessonsDb.find(Lesson::_id eq id).firstOrNull()
                ?: return@delete call.respondText(
                    "Lesson not found", status = HttpStatusCode.NotFound
                )
            lessonsDb.deleteOne(Lesson::_id eq id)
            call.respondText(
                "Lesson deleted correctly", status = HttpStatusCode.OK
            )
            val schedule = schedulesDb.find(Schedule::name eq lesson!!.name).toList()
            schedule.forEach{ scheduleDelete ->
                schedulesDb.deleteOne(Schedule::_id eq scheduleDelete._id)
            }
        }
        put("{id}") {
            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            lessonsDb.find(Lesson::_id eq id) ?: return@put call.respondText(
                "No element with id $id",
                status = HttpStatusCode.NotFound
            )
            val newLesson = call.receive<Lesson>()
            val lesson = lessonsDb.findOne(Lesson::_id eq id)
            lessonsDb.updateOne(Lesson::_id eq id, newLesson)
            call.respondText(
                "Element updates correctly",
                status = HttpStatusCode.Created
            )
            val schedule = schedulesDb.find(Schedule::name eq lesson!!.name).toList()
            schedule.forEach{ scheduleUdpate ->
                val newSchedule = Schedule(newLesson.name, scheduleUdpate.typeoflesson, scheduleUdpate.time, scheduleUdpate.dayofweek, scheduleUdpate.typeofweek, scheduleUdpate.group, scheduleUdpate.teacher, scheduleUdpate.audience, scheduleUdpate._id)
                schedulesDb.updateOne(Schedule::_id eq scheduleUdpate._id, newSchedule)
            }
        }
    }
}