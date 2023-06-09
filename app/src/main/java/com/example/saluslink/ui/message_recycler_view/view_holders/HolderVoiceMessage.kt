package com.example.saluslink.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.ui.message_recycler_view.views.MessageView
import com.example.saluslink.utilits.AppVoicePlayer
import com.example.saluslink.utilits.asTime
import com.example.saluslink.utilits.uid

class HolderVoiceMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolderInterface{

    private val mAppVoicePlayer = AppVoicePlayer()

    private val blockReceivedVoice: ConstraintLayout = view.findViewById(R.id.block_received_voice_message)
    private val blockUserVoice: ConstraintLayout = view.findViewById(R.id.block_user_voice_message)
    private val chatUserVoiceMessageTime: TextView = view.findViewById(R.id.chat_user_voice_message_time)
    private val chatReceivedVoiceMessageTime: TextView = view.findViewById(R.id.chat_received_voice_message_time)

    private val chatReceivedBtnPlay: ImageView = view.findViewById(R.id.chat_received_btn_play)
    private val chatUserBtnPlay: ImageView = view.findViewById(R.id.chat_user_btn_play)
    private val chatReceivedBtnPause: ImageView = view.findViewById(R.id.chat_received_btn_pause)
    private val chatUserBtnPause: ImageView = view.findViewById(R.id.chat_user_btn_pause)

    override fun drawMessage(view: MessageView) {
        if (view.from == uid){
            blockReceivedVoice.visibility = View.GONE
            blockUserVoice.visibility = View.VISIBLE
            chatUserVoiceMessageTime.text = view.timeStamp.asTime()
        } else{
            blockReceivedVoice.visibility = View.VISIBLE
            blockUserVoice.visibility = View.GONE
            chatReceivedVoiceMessageTime.text = view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {
        mAppVoicePlayer.init()
        if (view.from == uid){
            chatUserBtnPlay.setOnClickListener {
                chatUserBtnPlay.visibility = View.GONE
                chatUserBtnPause.visibility = View.VISIBLE
                chatUserBtnPause.setOnClickListener {
                    stop {
                        chatUserBtnPlay.visibility = View.VISIBLE
                        chatUserBtnPause.visibility = View.GONE
                    }
                }
                play(view){
                    chatUserBtnPlay.visibility = View.VISIBLE
                    chatUserBtnPause.visibility = View.GONE
                }
            }
        } else{
            chatReceivedBtnPlay.setOnClickListener {
                chatReceivedBtnPlay.visibility = View.GONE
                chatReceivedBtnPause.visibility = View.VISIBLE
                chatReceivedBtnPause.setOnClickListener {
                    stop {
                        chatReceivedBtnPlay.visibility = View.VISIBLE
                        chatReceivedBtnPause.visibility = View.GONE
                    }
                }
                play(view){
                    chatReceivedBtnPlay.visibility = View.VISIBLE
                    chatReceivedBtnPause.visibility = View.GONE
                }
            }
        }
    }

    private fun play(view: MessageView, function: () -> Unit) {
        mAppVoicePlayer.play(view.id, view.fileUrl){
            function()
        }
    }

    private fun stop(function: () -> Unit){
        mAppVoicePlayer.stop { function() }
    }

    override fun onDetach() {
        chatUserBtnPlay.setOnClickListener(null)
        chatReceivedBtnPlay.setOnClickListener(null)
        mAppVoicePlayer.release()
    }
}