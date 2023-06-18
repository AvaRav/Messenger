package com.example.saluslink.models

data class CommonModel(
    val id: String = "",
    var name: String = "",
    var status: String = "",
    var surname: String = "",
    var fullname: String = "",
    var specialization: String = "",
    var workplace: String = "",
    var education: String = "",
    var experience: String = "",
    var photoUrl: String = "empty",
    var direction: String = "",
    var themes: String = "",
    var aboutTheTopic: String = "",

    var text: String = "",
    var type: String = "",
    var from: String = "",
    var timeStamp: Any = "",
    var fileUrl: String = "empty",

    var lastMessage: String = "",

    var title: String = "",

    ) {
    override fun equals(other: Any?): Boolean {
        return (other as CommonModel).id == id
    }
}
