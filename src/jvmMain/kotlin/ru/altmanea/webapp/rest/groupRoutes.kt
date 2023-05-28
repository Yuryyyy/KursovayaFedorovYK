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
import ru.altmanea.webapp.data.Group
import ru.altmanea.webapp.data.Schedule
import ru.altmanea.webapp.repo.groupsDb
import ru.altmanea.webapp.repo.schedulesDb
import java.util.*

fun Route.groupRoutes() {
    route(Config.groupsPath) {
        get {
            val groups = groupsDb.find().toList() as List<Group>
            if (groups.isEmpty())
                return@get call.respondText(
                    "Groups not found", status = HttpStatusCode.NotFound
                )
            call.respond(groups)
        }
        post {
            val group = call.receive<Group>()
            val groupId = Group(group.name, UUID.randomUUID().toString())
            if (groupsDb.find(Group::name eq group.name).firstOrNull() != null)
                return@post call.respondText(
                    "The group already exists", status = HttpStatusCode.BadRequest
                )
            groupsDb.insertOne(groupId)
            call.respondText(
                "Group stored correctly", status = HttpStatusCode.Created
            )
        }
        delete("{id}") {
            val id = call.parameters["id"]
                ?: return@delete call.respondText(
                    "Missing or malformed id", status = HttpStatusCode.BadRequest
                )
            val group = groupsDb.findOne(Group::_id eq id)
            groupsDb.find(Group::_id eq id).firstOrNull()
                ?: return@delete call.respondText(
                    "Group not found", status = HttpStatusCode.NotFound
                )
            groupsDb.deleteOne(Group::_id eq id)
            call.respondText(
                "Group deleted correctly", status = HttpStatusCode.OK
            )
            val schedule = schedulesDb.find(Schedule::group eq group!!.name).toList()
            schedule.forEach{ scheduleDelete ->
                schedulesDb.deleteOne(Schedule::_id eq scheduleDelete._id)
            }
        }
        put("{id}") {
            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            groupsDb.find(Group::_id eq id) ?: return@put call.respondText(
                "No element with id $id",
                status = HttpStatusCode.NotFound
            )
            val newGroup = call.receive<Group>()
            val group = groupsDb.findOne(Group::_id eq id)
            groupsDb.updateOne(Group::_id eq id, newGroup)
            call.respondText(
                "Element updates correctly",
                status = HttpStatusCode.Created
            )
            val schedule = schedulesDb.find(Schedule::group eq group!!.name).toList()
            schedule.forEach{ scheduleUdpate ->
                val newSchedule = Schedule(scheduleUdpate.name, scheduleUdpate.typeoflesson, scheduleUdpate.time, scheduleUdpate.dayofweek, scheduleUdpate.typeofweek, newGroup.name, scheduleUdpate.teacher, scheduleUdpate.audience, scheduleUdpate._id)
                schedulesDb.updateOne(Schedule::_id eq scheduleUdpate._id, newSchedule)
            }
        }
    }
}