package com.example.saluslink.ui.fragments.message_recycler_view.views

import com.example.saluslink.models.CommonModel
import com.example.saluslink.utilits.TYPE_MESSAGE_IMAGE

class AppViewFactory {
    companion object{
        fun getView(message: CommonModel):MessageView{
            return when(message.type){
                TYPE_MESSAGE_IMAGE ->ViewImageMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl
                )
                else -> ViewTextMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl,
                    message.text
                )
            }
        }
    }
}