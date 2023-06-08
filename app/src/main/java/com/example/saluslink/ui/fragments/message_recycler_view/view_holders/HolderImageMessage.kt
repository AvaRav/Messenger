package com.example.saluslink.ui.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R

class HolderImageMessage(view: View) : RecyclerView.ViewHolder(view) {
    val blockReceivedImage: ConstraintLayout = view.findViewById(R.id.block_received_image_message)
    val blockUserImage: ConstraintLayout = view.findViewById(R.id.block_user_image_message)
    val chatUserImage: ImageView = view.findViewById(R.id.chat_user_image)
    val chatReceivedImage : ImageView = view.findViewById(R.id.chat_received_image)
    val chatUserImageMessageTime: TextView = view.findViewById(R.id.chat_user_image_message_time)
    val chatReceivedImageMessageTime: TextView = view.findViewById(R.id.chat_received_image_message_time)
}