package com.example.saluslink.ui.fragments.single_chat

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.ui.fragments.message_recycler_view.view_holders.AppHolderFactory
import com.example.saluslink.ui.fragments.message_recycler_view.view_holders.HolderImageMessage
import com.example.saluslink.ui.fragments.message_recycler_view.view_holders.HolderTextMessage
import com.example.saluslink.ui.fragments.message_recycler_view.views.MessageView
import com.example.saluslink.utilits.asTime
import com.example.saluslink.utilits.downloadAndSetImage
import com.example.saluslink.utilits.uid

class SingleChatAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mlistMessagesCache = mutableListOf<MessageView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return mlistMessagesCache[position].getTypeView()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HolderImageMessage -> drawMessageImage(holder, position)
            is HolderTextMessage -> drawMessageText(holder, position)
            else -> {}
        }
    }

    private fun drawMessageImage(holder: HolderImageMessage, position: Int) {
        if (mlistMessagesCache[position].from == uid){
            holder.blockReceivedImage.visibility = View.GONE
            holder.blockUserImage.visibility = View.VISIBLE
            holder.chatUserImage.downloadAndSetImage(mlistMessagesCache[position].fileUrl)
            holder.chatUserImageMessageTime.text = mlistMessagesCache[position].timeStamp.asTime()
        } else{
            holder.blockReceivedImage.visibility = View.VISIBLE
            holder.blockUserImage.visibility = View.GONE
            holder.chatReceivedImage.downloadAndSetImage(mlistMessagesCache[position].fileUrl)
            holder.chatReceivedImageMessageTime.text = mlistMessagesCache[position].timeStamp.asTime()
        }
    }

    private fun drawMessageText(holder: HolderTextMessage, position: Int) {
        if (mlistMessagesCache[position].from == uid){
            holder.blockUserMessage.visibility = View.VISIBLE
            holder.blockReceivedMessage.visibility = View.GONE
            holder.chatUserMessage.text = mlistMessagesCache[position].text
            holder.chatUserMessageTime.text = mlistMessagesCache[position].timeStamp.asTime()
        } else{
            holder.blockUserMessage.visibility = View.GONE
            holder.blockReceivedMessage.visibility = View.VISIBLE
            holder.chatReceivedMessage.text = mlistMessagesCache[position].text
            holder.chatReceivedMessageTime.text = mlistMessagesCache[position].timeStamp.asTime()
        }
    }

    override fun getItemCount(): Int = mlistMessagesCache.size

    fun addItem(item: MessageView, toBottom: Boolean){
        if (toBottom){
            if (!mlistMessagesCache.contains(item)){
                mlistMessagesCache.add(item)
                notifyItemInserted(mlistMessagesCache.size)
            }
        } else {
            if (!mlistMessagesCache.contains(item)) {
                mlistMessagesCache.add(item)
                mlistMessagesCache.sortBy { it.timeStamp }
                notifyItemInserted(0)
            }
        }
    }
}


