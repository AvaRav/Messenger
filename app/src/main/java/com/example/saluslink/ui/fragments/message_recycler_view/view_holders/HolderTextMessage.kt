package com.example.saluslink.ui.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R

class HolderTextMessage(view:View):RecyclerView.ViewHolder(view) {
    val blockUserMessage: ConstraintLayout = view.findViewById(R.id.block_user_message)
    val chatUserMessage: TextView = view.findViewById(R.id.chat_user_message)
    val chatUserMessageTime: TextView = view.findViewById(R.id.chat_user_message_time)
    val blockReceivedMessage: ConstraintLayout = view.findViewById(R.id.block_received_message)
    val chatReceivedMessage: TextView = view.findViewById(R.id.chat_received_message)
    val chatReceivedMessageTime: TextView = view.findViewById(R.id.chat_received_message_time)
}