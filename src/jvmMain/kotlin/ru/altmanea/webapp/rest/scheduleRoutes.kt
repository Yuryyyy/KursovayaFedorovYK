package ru.altmanea.webapp.rest

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.eq
import org.litote.kmongo.updateOne
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Schedule
import ru.altmanea.webapp.repo.schedulesDb
import java.util.*

fun Route.scheduleRoutes() {
    route(Config.schedulePath) {
        get {
            val schedules = schedulesDb.find().toList() as List<Schedule>
            if (schedules.isEmpty())
                return@get call.respondText(
                    "Schedules not found", status = HttpStatusCode.NotFound
                )
            call.respond(schedules)
        }
        post {
            val schedule = call.receive<Schedule>()
            val scheduleId = Schedule(schedule.name, schedule.typeoflesson, schedule.time, schedule.dayofweek, schedule.typeofweek, schedule.group, schedule.teacher, schedule.audience, UUID.randomUUID().toString())
            if ((schedulesDb.find(Schedule::name eq schedule.name).firstOrNull() != null) && (schedulesDb.find(Schedule::typeoflesson eq schedule.typeoflesson).firstOrNull() != null) && (schedulesDb.find(Schedule::time eq schedule.time).firstOrNull() != null) && (schedulesDb.find(Schedule::dayofweek eq schedule.dayofweek).firstOrNull() != null) && (schedulesDb.find(Schedule::typeofweek eq schedule.typeofweek).firstOrNull() != null) && (schedulesDb.find(Schedule::group eq schedule.group).firstOrNull() != null) && (schedulesDb.find(Schedule::teacher eq schedule.teacher).firstOrNull() != null) && (schedulesDb.find(Schedule::audience eq schedule.audience).firstOrNull() != null))
                return@post call.respondText(
                    "The schedule already exists", status = HttpStatusCode.BadRequest
                )
            schedulesDb.insertOne(scheduleId)
            call.respondText(
                "Schedule stored correctly", status = HttpStatusCode.Created
            )
        }
        delete("{id}") {
            val id = call.parameters["id"]
                ?: return@delete call.respondText(
                    "Missing or malformed id", status = HttpStatusCode.BadRequest
                )
            schedulesDb.find(Schedule::_id eq id).firstOrNull()
                ?: return@delete call.respondText(
                    "Schedule not found", status = HttpStatusCode.NotFound
                )
            schedulesDb.deleteOne(Schedule::_id eq id)
            call.respondText(
                "Schedule deleted correctly", status = HttpStatusCode.OK
            )
        }
        put("{id}") {
            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            schedulesDb.find(Schedule::_id eq id) ?: return@put call.respondText(
                "No element with id $id",
                status = HttpStatusCode.NotFound
            )
            val newSchedule = call.receive<Schedule>()
            schedulesDb.updateOne(Schedule::_id eq id, newSchedule)
            call.respondText(
                "Element updates correctly",
                status = HttpStatusCode.Created
            )
        }
    }
}