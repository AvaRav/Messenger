package com.example.saluslink.ui.message_recycler_view.views

import android.graphics.drawable.Drawable
import com.example.saluslink.models.CommonModel

interface MessageView {
    val id: String
    val from: String
    val timeStamp: String
    val fileUrl: String
    val text: String
    val photoUrl: String
    val fullname: String

    companion object{
        val MESSAGE_IMAGE: Int
            get() = 0
        val MESSAGE_TEXT: Int
            get() = 1
        val MESSAGE_VOICE: Int
            get() = 2
        val MESSAGE_FILE: Int
            get() = 3
    }

    fun getTypeView(): Int
}