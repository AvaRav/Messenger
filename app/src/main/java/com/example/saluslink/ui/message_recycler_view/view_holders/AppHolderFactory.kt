package com.example.saluslink.ui.message_recycler_view.view_holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.models.CommonModel
import com.example.saluslink.ui.message_recycler_view.views.MessageView

class AppHolderFactory {
    companion object{
        // Фабрика для создания различных ViewHolder'ов в зависимости от типа сообщения
        fun getHolder(parent: ViewGroup, viewType: Int, isGroupChat: Boolean): RecyclerView.ViewHolder{
            return  when(viewType){
                MessageView.MESSAGE_IMAGE -> {
                    // ViewHolder для изображений в сообщениях
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.message_item_image, parent, false)
                    HolderImageMessage(view, isGroupChat)
                }
                MessageView.MESSAGE_VOICE -> {
                    // ViewHolder для голосовых сообщений
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.message_item_voice, parent, false)
                    HolderVoiceMessage(view)
                }
                MessageView.MESSAGE_FILE -> {
                    // ViewHolder для файлов
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.message_item_file, parent, false)
                    HolderFileMessage(view, isGroupChat)
                }
                else ->{
                    // ViewHolder для текстовых сообщений (по умолчанию)
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.message_item_text, parent, false)
                    HolderTextMessage(view, isGroupChat)
                }
            }
        }
    }
}
