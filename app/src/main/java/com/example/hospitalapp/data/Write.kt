package com.example.hospitalapp.data

import java.util.UUID

data class Write(
    val id: UUID = UUID.randomUUID(),
    var time: String ="",
    var enable: Boolean = true
) {
    constructor() : this(UUID.randomUUID())
    var clients: MutableList<Client>? = null
}