package com.example.saluslink.ui.fragments.message_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.models.CommonModel
import com.example.saluslink.ui.fragments.single_chat.SingleChatFragment
import com.example.saluslink.utilits.APP_ACTIVITY
import com.example.saluslink.utilits.downloadAndSetImage
import com.example.saluslink.utilits.replaceFragment
import de.hdodenhof.circleimageview.CircleImageView

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageListHolder>() {

    private val listItems = mutableListOf<CommonModel>()

    class MessageListHolder(view: View) : RecyclerView.ViewHolder(view){
        val itemName: TextView = view.findViewById(R.id.message_list_item_name)
        val itemLastMessage: TextView = view.findViewById(R.id.message_list_last_message)
        val itemPhoto: CircleImageView = view.findViewById(R.id.message_list_item_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageListHolder {
        // Создание ViewHolder для элемента списка сообщений
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_list_item, parent, false)
        val holder = MessageListHolder(view)

        // Установка обработчика нажатия на элемент списка, который открывает фрагмент SingleChatFragment для выбранного чата
        holder.itemView.setOnClickListener {
            APP_ACTIVITY.replaceFragment(SingleChatFragment(listItems[holder.adapterPosition]))
        }

        return holder
    }

    override fun onBindViewHolder(holder: MessageListHolder, position: Int) {
        // Заполнение элемента списка данными из модели
        holder.itemName.text = listItems[position].fullname
        holder.itemLastMessage.text = listItems[position].lastMessage
        holder.itemPhoto.downloadAndSetImage(listItems[position].photoUrl)
    }

    override fun getItemCount(): Int = listItems.size

    // Метод для обновления списка сообщений
    fun updateListItems(item: CommonModel){
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }
}
