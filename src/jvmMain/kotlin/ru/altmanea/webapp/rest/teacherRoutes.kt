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
import ru.altmanea.webapp.data.Schedule
import ru.altmanea.webapp.data.Teacher
import ru.altmanea.webapp.repo.schedulesDb
import ru.altmanea.webapp.repo.teachersDb
import java.util.*

fun Route.teacherRoutes() {
    route(Config.teachersPath) {
        get {
            val teachers = teachersDb.find().toList() as List<Teacher>
            if (teachers.isEmpty())
                return@get call.respondText(
                    "Teachers not found", status = HttpStatusCode.NotFound
                )
            call.respond(teachers)
        }
        post {
            val teacher = call.receive<Teacher>()
            val teacherId = Teacher(teacher.firstname, teacher.middleName, teacher.lastname, UUID.randomUUID().toString())
            if ((teachersDb.find(Teacher::firstname eq teacher.firstname).firstOrNull() != null) && (teachersDb.find(Teacher::middleName eq teacher.middleName).firstOrNull() != null) && (teachersDb.find(Teacher::lastname eq teacher.lastname).firstOrNull() != null))
                return@post call.respondText(
                    "The teacher already exists", status = HttpStatusCode.BadRequest
                )
            teachersDb.insertOne(teacherId)
            call.respondText(
                "Teacher stored correctly", status = HttpStatusCode.Created
            )
        }
        delete("{id}") {
            val id = call.parameters["id"]
                ?: return@delete call.respondText(
                    "Missing or malformed id", status = HttpStatusCode.BadRequest
                )
            val teacher = teachersDb.findOne(Teacher::_id eq id)
            teachersDb.find(Teacher::_id eq id).firstOrNull()
                ?: return@delete call.respondText(
                    "Teacher not found", status = HttpStatusCode.NotFound
                )
            teachersDb.deleteOne(Teacher::_id eq id)
            call.respondText(
                "Teacher deleted correctly", status = HttpStatusCode.OK
            )
            val schedule = schedulesDb.find(Schedule::teacher eq teacher!!.fullname()).toList()
            schedule.forEach{ scheduleDelete ->
                schedulesDb.deleteOne(Schedule::_id eq scheduleDelete._id)
            }
        }
        put("{id}") {
            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            teachersDb.find(Teacher::_id eq id) ?: return@put call.respondText(
                "No element with id $id",
                status = HttpStatusCode.NotFound
            )
            val newTeacher = call.receive<Teacher>()
            val teacher = teachersDb.findOne(Teacher::_id eq id)
            teachersDb.updateOne(Teacher::_id eq id, newTeacher)
            call.respondText(
                "Element updates correctly",
                status = HttpStatusCode.Created
            )
            val schedule = schedulesDb.find(Schedule::teacher eq teacher!!.fullname()).toList()
            schedule.forEach{ scheduleUdpate ->
                val newSchedule = Schedule(scheduleUdpate.name, scheduleUdpate.typeoflesson, scheduleUdpate.time, scheduleUdpate.dayofweek, scheduleUdpate.typeofweek, scheduleUdpate.group, newTeacher.fullname(), scheduleUdpate.audience, scheduleUdpate._id)
                schedulesDb.updateOne(Schedule::_id eq scheduleUdpate._id, newSchedule)
            }
        }
    }
}