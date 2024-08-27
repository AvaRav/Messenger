package com.example.saluslink.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.models.CommonModel
import com.example.saluslink.ui.fragments.UserProfileFragment
import com.example.saluslink.ui.message_recycler_view.views.MessageView
import com.example.saluslink.utilits.*

class HolderVoiceMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolderInterface {
    // Представления (Views) для отображения голосовых сообщений в пользовательском чате
    private val blockUserVoice: ConstraintLayout = view.findViewById(R.id.block_user_voice_message)
    private val chatUserVoiceMessageTime: TextView = view.findViewById(R.id.chat_user_voice_message_time)
    private val chatUserBtnPlay: ImageView = view.findViewById(R.id.chat_user_btn_play)
    private val chatUserBtnPause: ImageView = view.findViewById(R.id.chat_user_btn_pause)

    // Представления (Views) для отображения полученных голосовых сообщений в пользовательском чате
    private val chatReceivedVoiceMessageTime: TextView = view.findViewById(R.id.chat_received_voice_message_time)
    private val blockReceivedVoice: ConstraintLayout = view.findViewById(R.id.block_received_voice_message)
    private val chatReceivedBtnPlay: ImageView = view.findViewById(R.id.chat_received_btn_play)
    private val chatReceivedBtnPause: ImageView = view.findViewById(R.id.chat_received_btn_pause)

    private val mAppVoicePlayer = AppVoicePlayer()

    // Метод, отвечающий за отрисовку сообщения
    override fun drawMessage(view: MessageView) {
        if (view.from == uid) {
            // Отображение голосового сообщения пользователя в пользовательском чате
            blockReceivedVoice.visibility = View.GONE
            blockUserVoice.visibility = View.VISIBLE
            chatUserVoiceMessageTime.text = view.timeStamp.asTime()
        } else {
            // Отображение полученного голосового сообщения в пользовательском чате
            blockReceivedVoice.visibility = View.VISIBLE
            blockUserVoice.visibility = View.GONE
            chatReceivedVoiceMessageTime.text = view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {
        // Инициализация проигрывателя голосовых сообщений
        mAppVoicePlayer.init()

        if (view.from == uid) {
            // Обработчик нажатия на кнопку воспроизведения голосового сообщения пользователя в пользовательском чате
            chatUserBtnPlay.setOnClickListener {
                chatUserBtnPlay.visibility = View.GONE
                chatUserBtnPause.visibility = View.VISIBLE

                // Обработчик нажатия на кнопку паузы воспроизведения голосового сообщения пользователя
                chatUserBtnPause.setOnClickListener {
                    stop {
                        chatUserBtnPlay.visibility = View.VISIBLE
                        chatUserBtnPause.visibility = View.GONE
                    }
                }

                // Воспроизведение голосового сообщения
                play(view) {
                    chatUserBtnPlay.visibility = View.VISIBLE
                    chatUserBtnPause.visibility = View.GONE
                }
            }
        } else {
            // Обработчик нажатия на кнопку воспроизведения полученного голосового сообщения в пользовательском чате
            chatReceivedBtnPlay.setOnClickListener {
                chatReceivedBtnPlay.visibility = View.GONE
                chatReceivedBtnPause.visibility = View.VISIBLE

                // Обработчик нажатия на кнопку паузы воспроизведения полученного голосового сообщения
                chatReceivedBtnPause.setOnClickListener {
                    stop {
                        chatReceivedBtnPlay.visibility = View.VISIBLE
                        chatReceivedBtnPause.visibility = View.GONE
                    }
                }

                // Воспроизведение голосового сообщения
                play(view) {
                    chatReceivedBtnPlay.visibility = View.VISIBLE
                    chatReceivedBtnPause.visibility = View.GONE
                }
            }
        }
    }

    // Метод для воспроизведения голосового сообщения
    private fun play(view: MessageView, function: () -> Unit) {
        mAppVoicePlayer.play(view.id, view.fileUrl) {
            function()
        }
    }

    // Метод для остановки воспроизведения голосового сообщения
    private fun stop(function: () -> Unit) {
        mAppVoicePlayer.stop { function() }
    }

    override fun onDetach() {
        // Отмена обработчиков нажатия кнопок воспроизведения голосовых сообщений
        chatUserBtnPlay.setOnClickListener(null)
        chatReceivedBtnPlay.setOnClickListener(null)

        // Освобождение проигрывателя голосовых сообщений
        mAppVoicePlayer.release()
    }
}
