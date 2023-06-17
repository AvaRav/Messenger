package com.example.saluslink.ui.fragments.single_chat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.ui.message_recycler_view.view_holders.*
import com.example.saluslink.ui.message_recycler_view.views.MessageView

class SingleChatAdapter(private val isGroupChat: Boolean): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListMessagesCache = mutableListOf<MessageView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType, isGroupChat)
    }

    override fun getItemViewType(position: Int): Int {
        return mListMessagesCache[position].getTypeView()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MessageHolderInterface).drawMessage(mListMessagesCache[position])
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolderInterface).onAttach(mListMessagesCache[holder.adapterPosition])
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolderInterface).onDetach()
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int = mListMessagesCache.size

    fun addItem(item: MessageView, toBottom: Boolean){
        if (toBottom){
            if (!mListMessagesCache.contains(item)){
                mListMessagesCache.add(item)
                notifyItemInserted(mListMessagesCache.size)
            }
        } else {
            if (!mListMessagesCache.contains(item)) {
                mListMessagesCache.add(item)
                mListMessagesCache.sortBy { it.timeStamp }
                notifyItemInserted(0)
            }
        }
    }
}


