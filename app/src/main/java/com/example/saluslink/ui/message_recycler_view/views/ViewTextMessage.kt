package com.example.saluslink.ui.message_recycler_view.views

class ViewTextMessage(
    override val id: String,
    override val from: String,
    override val timeStamp: String,
    override val fileUrl: String = "",
    override val text: String,
    override val photoUrl: String = "",
    override val fullname: String = "",
) : MessageView {

    override fun getTypeView(): Int {
        return MessageView.MESSAGE_TEXT
    }


    override fun equals(other: Any?): Boolean {
        return (other as MessageView).id == id
    }
}