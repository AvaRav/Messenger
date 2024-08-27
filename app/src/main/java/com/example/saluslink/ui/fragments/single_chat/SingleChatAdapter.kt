package com.example.saluslink.ui.fragments.single_chat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.ui.message_recycler_view.view_holders.*
import com.example.saluslink.ui.message_recycler_view.views.MessageView

class SingleChatAdapter(private val isGroupChat: Boolean): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListMessagesCache = mutableListOf<MessageView>()

    // Создание нового экземпляра ViewHolder для отображения элементов списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType, isGroupChat)
    }

    // Получение типа элемента списка для определения его вида
    override fun getItemViewType(position: Int): Int {
        return mListMessagesCache[position].getTypeView()
    }

    // Привязка данных к ViewHolder и отображение элемента списка
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MessageHolderInterface).drawMessage(mListMessagesCache[position])
    }

    // Вызывается, когда ViewHolder присоединяется к RecyclerView
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolderInterface).onAttach(mListMessagesCache[holder.adapterPosition])
        super.onViewAttachedToWindow(holder)
    }

    // Вызывается, когда ViewHolder отсоединяется от RecyclerView
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolderInterface).onDetach()
        super.onViewDetachedFromWindow(holder)
    }

    // Возвращает количество элементов в списке
    override fun getItemCount(): Int = mListMessagesCache.size

    // Добавление нового элемента в список сообщений
    fun addItem(item: MessageView, toBottom: Boolean){
        if (toBottom){
            // Добавление элемента в конец списка
            if (!mListMessagesCache.contains(item)){
                mListMessagesCache.add(item)
                notifyItemInserted(mListMessagesCache.size)
            }
        } else {
            // Добавление элемента в начало списка и сортировка по временной метке
            if (!mListMessagesCache.contains(item)) {
                mListMessagesCache.add(item)
                mListMessagesCache.sortBy { it.timeStamp }
                notifyItemInserted(0)
            }
        }
    }
}
