package com.example.hospitalapp.data


import java.util.*

data class Client(
    val id : UUID = UUID.randomUUID(),
    var firstName : String="",
    var middleName : String="",
    var lastName : String="",
    var reason : String="",
    var haveACard : Boolean = true
)
