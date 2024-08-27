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

class HolderImageMessage(view: View, private val isGroupChat: Boolean) : RecyclerView.ViewHolder(view), MessageHolderInterface {
    // Представления (Views) для отображения сообщений с изображениями в пользовательском чате
    private val blockUserImage: ConstraintLayout = view.findViewById(R.id.block_user_image_message)
    private val chatUserImage: ImageView = view.findViewById(R.id.chat_user_image)
    private val chatUserImageMessageTime: TextView = view.findViewById(R.id.chat_user_image_message_time)

    // Представления (Views) для отображения полученных сообщений с изображениями в пользовательском чате
    private val blockReceivedImage: ConstraintLayout = view.findViewById(R.id.block_received_image_message)
    private val chatReceivedImage : ImageView = view.findViewById(R.id.chat_received_image)
    private val chatReceivedImageMessageTime: TextView = view.findViewById(R.id.chat_received_image_message_time)

    // Представления (Views) для отображения полученных сообщений с изображениями в групповом чате
    private val groupBlockReceivedImage: ConstraintLayout = view.findViewById(R.id.group_block_received_image_message)
    private val groupChatReceivedImage : ImageView = view.findViewById(R.id.group_received_image)
    private val groupChatReceivedImageMessageTime: TextView = view.findViewById(R.id.group_received_image_message_time)

    // Фотография пользователя в групповом чате
    private val groupUserPhoto: CircleImageView = view.findViewById(R.id.photo_group_chat)

    // Метод, отвечающий за отрисовку сообщения
    override fun drawMessage(view: MessageView) {
        if (view.from == uid) {
            // Отображение сообщения пользователя в пользовательском чате
            blockReceivedImage.visibility = View.GONE
            blockUserImage.visibility = View.VISIBLE
            groupBlockReceivedImage.visibility = View.GONE
            groupUserPhoto.visibility = View.GONE
            chatUserImage.downloadAndSetImage(view.fileUrl)
            chatUserImageMessageTime.text = view.timeStamp.asTime()
        } else {
            if (isGroupChat) {
                // Отображение полученного сообщения в групповом чате
                groupBlockReceivedImage.visibility = View.VISIBLE
                blockReceivedImage.visibility = View.GONE
                blockUserImage.visibility = View.GONE
                groupUserPhoto.visibility = View.VISIBLE
                groupChatReceivedImage.downloadAndSetImage(view.fileUrl)
                groupChatReceivedImageMessageTime.text = view.timeStamp.asTime()
                receivePhotoAndName(view)
            } else {
                // Отображение полученного сообщения в пользовательском чате
                groupBlockReceivedImage.visibility = View.GONE
                blockReceivedImage.visibility = View.VISIBLE
                blockUserImage.visibility = View.GONE
                groupUserPhoto.visibility = View.GONE
                chatReceivedImage.downloadAndSetImage(view.fileUrl)
                chatReceivedImageMessageTime.text = view.timeStamp.asTime()
            }
        }
    }

    // Обработчик нажатия на фотографию пользователя в групповом чате для отображения профиля пользователя
    fun goToProfileUser(userProfile: CommonModel) {
        groupUserPhoto.setOnClickListener {
            APP_ACTIVITY.replaceFragment(UserProfileFragment(userProfile))
        }
    }

    // Получение фотографии и имени отправителя сообщения в групповом чате
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

                    // Извлечение данных пользователя из Firebase Realtime Database
                    val photoUrl = dataSnapshot.child("photoUrl").getValue(String::class.java)
                    val name = dataSnapshot.child("name").getValue(String::class.java)
                    val surname = dataSnapshot.child("surname").getValue(String::class.java)
                    val specialization = dataSnapshot.child("specialization").getValue(String::class.java)
                    val education = dataSnapshot.child("education").getValue(String::class.java)
                    val workplace = dataSnapshot.child("workplace").getValue(String::class.java)
                    val experience = dataSnapshot.child("experience").getValue(String::class.java)

                    // Загрузка фотографии пользователя в групповом чате
                    groupUserPhoto.downloadAndSetImage(photoUrl)

                    // Создание объекта CommonModel, представляющего профиль пользователя
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
                        photoUrl ?: ""
                    )

                    // Переход на профиль пользователя при нажатии на его фотографию в групповом чате
                    goToProfileUser(userProfile)

                } catch (e: Exception) { }
            }
        }
    }

    override fun onAttach(view: MessageView) {}

    override fun onDetach() {}
}
