package com.example.saluslink.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.models.CommonModel
import com.example.saluslink.utilits.DiffUtilCallback
import com.example.saluslink.utilits.asTime
import com.example.saluslink.utilits.downloadAndSetImage
import com.example.saluslink.utilits.uid
import java.text.SimpleDateFormat
import java.util.*

class SingleChatAdapter: RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var mlistMessagesCache = mutableListOf<CommonModel>()

    class SingleChatHolder(view: View): RecyclerView.ViewHolder(view){

        //Text
        val blockUserMessage:ConstraintLayout = view.findViewById(R.id.block_user_message)
        val chatUserMessage:TextView = view.findViewById(R.id.chat_user_message)
        val chatUserMessageTime:TextView = view.findViewById(R.id.chat_user_message_time)
        val blockReceivedMessage:ConstraintLayout = view.findViewById(R.id.block_received_message)
        val chatReceivedMessage:TextView = view.findViewById(R.id.chat_received_message)
        val chatReceivedMessageTime:TextView = view.findViewById(R.id.chat_received_message_time)

        //Image
        val blockReceivedImage: ConstraintLayout = view.findViewById(R.id.block_received_image_message)
        val blockUserImage: ConstraintLayout = view.findViewById(R.id.block_user_image_message)
        val chatUserImage: ImageView = view.findViewById(R.id.chat_user_image)
        val chatReceivedImage : ImageView = view.findViewById(R.id.chat_received_image)
        val chatUserImageMessageTime: TextView = view.findViewById(R.id.chat_user_image_message_time)
        val chatReceivedImageMessageTime: TextView = view.findViewById(R.id.chat_received_image_message_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return SingleChatHolder(view)
    }

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        when(mlistMessagesCache[position].type){
            "text" -> drawMessageText(holder, position)
            "image" -> drawMessageImage(holder, position)
        }
    }

    private fun drawMessageImage(holder: SingleChatAdapter.SingleChatHolder, position: Int) {
        holder.blockUserMessage.visibility = View.GONE
        holder.blockReceivedMessage.visibility = View.GONE
        if (mlistMessagesCache[position].from == uid){
            holder.blockReceivedImage.visibility = View.GONE
            holder.blockUserImage.visibility = View.VISIBLE
            holder.chatUserImage.downloadAndSetImage(mlistMessagesCache[position].imageUrl)
            holder.chatUserImageMessageTime.text = mlistMessagesCache[position].timeStamp.toString().asTime()
        } else{
            holder.blockReceivedImage.visibility = View.VISIBLE
            holder.blockUserImage.visibility = View.GONE
            holder.chatReceivedImage.downloadAndSetImage(mlistMessagesCache[position].imageUrl)
            holder.chatReceivedImageMessageTime.text = mlistMessagesCache[position].timeStamp.toString().asTime()
        }
    }

    private fun drawMessageText(holder: SingleChatAdapter.SingleChatHolder, position: Int) {
        holder.blockReceivedImage.visibility = View.GONE
        holder.blockUserImage.visibility = View.GONE
        if (mlistMessagesCache[position].from == uid){
            holder.blockUserMessage.visibility = View.VISIBLE
            holder.blockReceivedMessage.visibility = View.GONE
            holder.chatUserMessage.text = mlistMessagesCache[position].text
            holder.chatUserMessageTime.text = mlistMessagesCache[position].timeStamp.toString().asTime()
        } else{
            holder.blockUserMessage.visibility = View.GONE
            holder.blockReceivedMessage.visibility = View.VISIBLE
            holder.chatReceivedMessage.text = mlistMessagesCache[position].text
            holder.chatReceivedMessageTime.text = mlistMessagesCache[position].timeStamp.toString().asTime()
        }
    }

    override fun getItemCount(): Int = mlistMessagesCache.size

    fun addItem(item: CommonModel, toBottom: Boolean){
        if (toBottom){
            if (!mlistMessagesCache.contains(item)){
                mlistMessagesCache.add(item)
                notifyItemInserted(mlistMessagesCache.size)
            }
        } else {
            if (!mlistMessagesCache.contains(item)) {
                mlistMessagesCache.add(item)
                mlistMessagesCache.sortBy { it.timeStamp.toString() }
                notifyItemInserted(0)
            }
        }
    }
}


