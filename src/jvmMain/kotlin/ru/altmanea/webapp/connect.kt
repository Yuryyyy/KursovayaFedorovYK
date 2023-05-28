package ru.altmanea.webapp

import org.litote.kmongo.KMongo

val client = KMongo
    .createClient("mongodb://root:example@127.0.0.1:27017")
val mongoDatabase = client.getDatabase("test")