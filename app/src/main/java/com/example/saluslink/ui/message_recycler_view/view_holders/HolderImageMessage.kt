package com.example.saluslink.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.ui.message_recycler_view.views.MessageView
import com.example.saluslink.utilits.asTime
import com.example.saluslink.utilits.downloadAndSetImage
import com.example.saluslink.utilits.uid

class HolderImageMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolderInterface {
    private val blockReceivedImage: ConstraintLayout = view.findViewById(R.id.block_received_image_message)
    private val blockUserImage: ConstraintLayout = view.findViewById(R.id.block_user_image_message)
    private val chatUserImage: ImageView = view.findViewById(R.id.chat_user_image)
    private val chatReceivedImage : ImageView = view.findViewById(R.id.chat_received_image)
    private val chatUserImageMessageTime: TextView = view.findViewById(R.id.chat_user_image_message_time)
    private val chatReceivedImageMessageTime: TextView = view.findViewById(R.id.chat_received_image_message_time)

    override fun drawMessage(view: MessageView) {
        if (view.from == uid){
            blockReceivedImage.visibility = View.GONE
            blockUserImage.visibility = View.VISIBLE
            chatUserImage.downloadAndSetImage(view.fileUrl)
            chatUserImageMessageTime.text = view.timeStamp.asTime()
        } else{
            blockReceivedImage.visibility = View.VISIBLE
            blockUserImage.visibility = View.GONE
            chatReceivedImage.downloadAndSetImage(view.fileUrl)
            chatReceivedImageMessageTime.text = view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {
    }

    override fun onDetach() {
    }
}