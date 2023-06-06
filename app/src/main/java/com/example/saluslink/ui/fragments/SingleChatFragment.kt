package com.example.saluslink.ui.fragments

import android.graphics.Color
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.saluslink.R
import com.example.saluslink.models.CommonModel
import com.example.saluslink.models.User
import com.example.saluslink.utilits.*
import com.google.firebase.database.DatabaseReference

class SingleChatFragment(private val model: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mListenerInfoHeader: AppValueEventListener
    private lateinit var mReceivingUser: User
    private lateinit var mHeaderInfo: View
    private lateinit var mRefUser: DatabaseReference

    override fun onResume() {
        super.onResume()
        mHeaderInfo = requireView().findViewById(R.id.message_header_block)
        mListenerInfoHeader = AppValueEventListener {
            mReceivingUser = it.getUserModel()
            initInfoHeader()
        }
        mRefUser = ref_database_root.child("users").child(model.id)
        mRefUser.addValueEventListener(mListenerInfoHeader)

        requireView().findViewById<ImageView>(R.id.chat_btn_send_message).setOnClickListener {
            val message = requireView().findViewById<EditText>(R.id.chat_input_message).text.toString()
            if (message.isEmpty()){
                showToast("Введите сообщение")
            } else sendMessage(message, model.id, "text"){
                requireView().findViewById<EditText>(R.id.chat_input_message).setText("")
            }
        }
    }

    private fun initInfoHeader() {
        mHeaderInfo.findViewById<ImageView>(R.id.user_photo).downloadAndSetImage(mReceivingUser.photoUrl)
        mHeaderInfo.findViewById<TextView>(R.id.user_name).text = mReceivingUser.fullname
        mHeaderInfo.findViewById<TextView>(R.id.message_text_online_offline).text = mReceivingUser.status
    }

    override fun onPause() {
        super.onPause()
        mRefUser.removeEventListener(mListenerInfoHeader)
    }
}