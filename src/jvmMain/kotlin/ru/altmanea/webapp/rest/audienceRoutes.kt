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
import ru.altmanea.webapp.data.Audience
import ru.altmanea.webapp.data.Schedule
import ru.altmanea.webapp.repo.audiencesDb
import ru.altmanea.webapp.repo.schedulesDb
import java.util.*

fun Route.audienceRoutes() {
    route(Config.audiencesPath) {
        get {
            val audiences = audiencesDb.find().toList() as List<Audience>
            if (audiences.isEmpty())
                return@get call.respondText(
                    "Audiences not found", status = HttpStatusCode.NotFound
                )
            call.respond(audiences)
        }
        post {
            val audience = call.receive<Audience>()
            val audienceId = Audience(audience.classroom, UUID.randomUUID().toString())
            if (audiencesDb.find(Audience::classroom eq audience.classroom).firstOrNull() != null)
                return@post call.respondText(
                    "The audience already exists", status = HttpStatusCode.BadRequest
                )
            audiencesDb.insertOne(audienceId)
            call.respondText(
                "Audience stored correctly", status = HttpStatusCode.Created
            )
        }
        delete("{id}") {
            val id = call.parameters["id"]
                ?: return@delete call.respondText(
                    "Missing or malformed id", status = HttpStatusCode.BadRequest
                )
            val audience = audiencesDb.findOne(Audience::_id eq id)
            audiencesDb.find(Audience::_id eq id).firstOrNull()
                ?: return@delete call.respondText(
                    "Audience not found", status = HttpStatusCode.NotFound
                )
            audiencesDb.deleteOne(Audience::_id eq id)
            call.respondText(
                "Audience deleted correctly", status = HttpStatusCode.OK
            )
            val schedule = schedulesDb.find(Schedule::audience eq audience!!.classroom).toList()
            schedule.forEach{ scheduleDelete ->
                schedulesDb.deleteOne(Schedule::_id eq scheduleDelete._id)
            }
        }
        put("{id}") {
            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            audiencesDb.find(Audience::_id eq id) ?: return@put call.respondText(
                "No element with id $id",
                status = HttpStatusCode.NotFound
            )
            val newAudience = call.receive<Audience>()
            val audience = audiencesDb.findOne(Audience::_id eq id)
            audiencesDb.updateOne(Audience::_id eq id, newAudience)
            call.respondText(
                "Element updates correctly",
                status = HttpStatusCode.Created
            )
            val schedule = schedulesDb.find(Schedule::audience eq audience!!.classroom).toList()
            schedule.forEach{ scheduleUdpate ->
                val newSchedule = Schedule(scheduleUdpate.name, scheduleUdpate.typeoflesson, scheduleUdpate.time, scheduleUdpate.dayofweek, scheduleUdpate.typeofweek, scheduleUdpate.group, scheduleUdpate.teacher, newAudience.classroom, scheduleUdpate._id)
                schedulesDb.updateOne(Schedule::_id eq scheduleUdpate._id, newSchedule)
            }
        }
    }
}