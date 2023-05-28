package ru.altmanea.webapp.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class Audience(
    val classroom: Int,
    val _id: String
)

val Audience.json
    get() = Json.encodeToString(this)

