package com.example.saluslink.models

data class User(
    val id: String = "",
    var name: String = "",
    var status: String = "",
    var surname: String = "",
    var fullname: String = "",
    var specialization: String = "",
    var workplace: String = "",
    var education: String = "",
    var experience: String = "",
    var photoUrl: String = "empty"
)