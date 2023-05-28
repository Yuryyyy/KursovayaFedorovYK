package ru.altmanea.webapp.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class Lesson(
    val name: String,
    val _id: String
)

val Lesson.json
    get() = Json.encodeToString(this)

