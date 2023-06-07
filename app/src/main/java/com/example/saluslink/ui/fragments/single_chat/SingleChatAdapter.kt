package com.example.saluslink.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.models.CommonModel
import com.example.saluslink.utilits.DiffUtilCallback
import com.example.saluslink.utilits.asTime
import com.example.saluslink.utilits.uid
import java.text.SimpleDateFormat
import java.util.*

class SingleChatAdapter: RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var mlistMessagesCache = mutableListOf<CommonModel>()
    private lateinit var mDiffResult: DiffUtil.DiffResult

    class SingleChatHolder(view: View): RecyclerView.ViewHolder(view){
        val blockUserMessage:ConstraintLayout = view.findViewById(R.id.block_user_message)
        val chatUserMessage:TextView = view.findViewById(R.id.chat_user_message)
        val chatUserMessageTime:TextView = view.findViewById(R.id.chat_user_message_time)

        val blockReceivedMessage:ConstraintLayout = view.findViewById(R.id.block_received_message)
        val chatReceivedMessage:TextView = view.findViewById(R.id.chat_received_message)
        val chatReceivedMessageTime:TextView = view.findViewById(R.id.chat_received_message_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return SingleChatHolder(view)
    }

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
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


