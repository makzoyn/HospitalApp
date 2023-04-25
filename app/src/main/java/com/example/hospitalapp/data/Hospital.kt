package com.example.hospitalapp.data

import java.util.*

data class Hospital(
    val id : UUID = UUID.randomUUID(),
    var name : String=""){
    constructor() : this(UUID.randomUUID())
    var doctors : MutableList<Doctor>? = null
}