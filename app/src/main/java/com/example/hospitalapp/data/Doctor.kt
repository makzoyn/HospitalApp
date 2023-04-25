package com.example.hospitalapp.data

import java.sql.Time
import java.util.Date
import java.util.UUID

data class Doctor(
    val id : UUID = UUID.randomUUID(),
    var name: String=""
){
    constructor() : this(UUID.randomUUID())
    var writes : MutableList<Write>? = null
}
