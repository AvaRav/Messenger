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
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HolderTextMessage(view:View, private val isGroupChat: Boolean):RecyclerView.ViewHolder(view), MessageHolderInterface{
    private val blockUserMessage: ConstraintLayout = view.findViewById(R.id.block_user_message)
    private val chatUserMessage: TextView = view.findViewById(R.id.chat_user_message)
    private val chatUserMessageTime: TextView = view.findViewById(R.id.chat_user_message_time)

    private val blockReceivedMessage: ConstraintLayout = view.findViewById(R.id.block_received_message)
    private val chatReceivedMessage: TextView = view.findViewById(R.id.chat_received_message)
    private val chatReceivedMessageTime: TextView = view.findViewById(R.id.chat_received_message_time)

    private val groupBlockReceivedMessage: ConstraintLayout = view.findViewById(R.id.group_block_received_message)
    private val groupChatReceivedMessage: TextView = view.findViewById(R.id.group_received_message)
    private val groupChatReceivedMessageTime: TextView = view.findViewById(R.id.group_received_message_time)

    private val groupUserPhoto: CircleImageView = view.findViewById(R.id.photo_group_chat)
    private val groupUserName: TextView = view.findViewById(R.id.name_group_chat_text)

    override fun drawMessage(view: MessageView) {
        if (view.from == uid){
            blockUserMessage.visibility = View.VISIBLE
            blockReceivedMessage.visibility = View.GONE
            groupBlockReceivedMessage.visibility = View.GONE
            groupUserPhoto.visibility = View.GONE
            chatUserMessage.text = view.text
            chatUserMessageTime.text = view.timeStamp.asTime()
        } else{
            if (isGroupChat) {
                blockUserMessage.visibility = View.GONE
                blockReceivedMessage.visibility = View.GONE
                groupBlockReceivedMessage.visibility = View.VISIBLE
                groupUserPhoto.visibility = View.VISIBLE
                groupChatReceivedMessage.text = view.text
                groupChatReceivedMessageTime.text = view.timeStamp.asTime()
                receivePhotoAndName(view)
            } else {
                blockUserMessage.visibility = View.GONE
                blockReceivedMessage.visibility = View.VISIBLE
                groupBlockReceivedMessage.visibility = View.GONE
                groupUserPhoto.visibility = View.GONE
                chatReceivedMessage.text = view.text
                chatReceivedMessageTime.text = view.timeStamp.asTime()
            }
        }
    }

    fun goToProfileUser(userProfile: CommonModel) {
        groupUserPhoto.setOnClickListener {
            APP_ACTIVITY.replaceFragment(UserProfileFragment(userProfile))
        }
    }

    fun receivePhotoAndName(view: MessageView) {
        if (view.from != uid) {
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val userId = view.from
            val userRef = database.reference.child("users").child(userId)

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val dataSnapshot = withContext(Dispatchers.IO) {
                        userRef.get().await()
                    }

                    val photoUrl = dataSnapshot.child("photoUrl").getValue(String::class.java)
                    val fullname = dataSnapshot.child("fullname").getValue(String::class.java)
                    val name = dataSnapshot.child("name").getValue(String::class.java)
                    val surname = dataSnapshot.child("surname").getValue(String::class.java)
                    val specialization = dataSnapshot.child("specialization").getValue(String::class.java)
                    val education = dataSnapshot.child("education").getValue(String::class.java)
                    val workplace = dataSnapshot.child("workplace").getValue(String::class.java)
                    val experience = dataSnapshot.child("experience").getValue(String::class.java)

                    groupUserPhoto.downloadAndSetImage(photoUrl)
                    groupUserName.text = fullname

                    val userProfile = CommonModel(
                        "",
                        name ?: "",
                        "",
                        surname ?: "",
                        "",
                        specialization ?: "",
                        workplace ?: "",
                        education ?: "",
                        experience ?: "",
                        photoUrl ?: "")

                    goToProfileUser(userProfile)

                } catch (e: Exception) { }
            }
        }
    }

    override fun onAttach(view: MessageView) {}

    override fun onDetach() {}
}