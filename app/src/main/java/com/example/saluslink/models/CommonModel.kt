package com.example.saluslink.models

data class CommonModel(
    val id: String = "",
    var name: String = "",
    var status: String = "",
    var surname: String = "",
    var specialization: String = "",
    var workplace: String = "",
    var education: String = "",
    var experience: String = "",
    var photoUrl: String = "empty"
)