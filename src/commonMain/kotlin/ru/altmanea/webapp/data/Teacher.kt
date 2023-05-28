package ru.altmanea.webapp.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class Teacher(
    val firstname: String,
    val middleName: String,
    val lastname: String,
    val _id: String
) {
    fun fullname() =
        "$firstname $middleName $lastname"
}

val Teacher.json
    get() = Json.encodeToString(this)

