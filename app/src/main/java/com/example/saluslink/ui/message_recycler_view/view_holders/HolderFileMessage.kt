package com.example.saluslink.ui.message_recycler_view.view_holders

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
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
import java.io.File

class HolderFileMessage(view: View, private val isGroupChat: Boolean) : RecyclerView.ViewHolder(view), MessageHolderInterface {
    // Views для сообщений с файлами пользователя
    private val blockUserFile: ConstraintLayout = view.findViewById(R.id.block_user_file_message)
    private val chatUserFileMessageTime: TextView = view.findViewById(R.id.chat_user_file_message_time)
    private val chatUserFilename: TextView = view.findViewById(R.id.chat_user_file_name)
    private val chatUserBtnDownload: ImageView = view.findViewById(R.id.chat_user_btn_download)
    private val chatUserProgressBar: ProgressBar = view.findViewById(R.id.user_progressBar)

    // Views для полученных файлов
    private val blockReceivedFile: ConstraintLayout = view.findViewById(R.id.block_received_file_message)
    private val chatReceivedFileMessageTime: TextView = view.findViewById(R.id.chat_received_file_message_time)
    private val chatReceivedFilename: TextView = view.findViewById(R.id.chat_received_file_name)
    private val chatReceivedBtnDownload: ImageView = view.findViewById(R.id.chat_received_btn_download)
    private val chatReceivedProgressBar: ProgressBar = view.findViewById(R.id.received_progressBar)

    // Views для полученных файлов в групповом чате
    private val groupBlockReceivedFile: ConstraintLayout = view.findViewById(R.id.group_block_received_file_message)
    private val groupChatReceivedFileMessageTime: TextView = view.findViewById(R.id.group_chat_received_file_message_time)
    private val groupChatReceivedFilename: TextView = view.findViewById(R.id.group_chat_received_file_name)
    private val groupChatReceivedBtnDownload: ImageView = view.findViewById(R.id.group_chat_received_btn_download)
    private val groupChatReceivedProgressBar: ProgressBar = view.findViewById(R.id.group_received_progressBar)

    // Views для фотографии и имени пользователя в групповом чате
    private val groupUserPhoto: CircleImageView = view.findViewById(R.id.photo_group_chat)
    private val groupUserName: TextView = view.findViewById(R.id.name_group_chat)

    override fun drawMessage(view: MessageView) {
        if (view.from == uid) {
            // Отображение сообщений с файлами пользователя
            blockReceivedFile.visibility = View.GONE
            blockUserFile.visibility = View.VISIBLE
            groupBlockReceivedFile.visibility = View.GONE
            groupUserPhoto.visibility = View.GONE
            chatUserFileMessageTime.text = view.timeStamp.asTime()
            chatUserFilename.text = view.text
        } else {
            if (isGroupChat) {
                // Отображение полученных файлов в групповом чате
                receivePhotoAndName(view)
                groupBlockReceivedFile.visibility = View.VISIBLE
                blockReceivedFile.visibility = View.GONE
                blockUserFile.visibility = View.GONE
                groupUserPhoto.visibility = View.VISIBLE
                groupChatReceivedFileMessageTime.text = view.timeStamp.asTime()
                groupChatReceivedFilename.text = view.text
            } else {
                // Отображение полученных файлов в приватном чате
                blockReceivedFile.visibility = View.VISIBLE
                blockUserFile.visibility = View.GONE
                groupBlockReceivedFile.visibility = View.GONE
                groupUserPhoto.visibility = View.GONE
                chatReceivedFileMessageTime.text = view.timeStamp.asTime()
                chatReceivedFilename.text = view.text
            }
        }
    }

    fun goToProfileUser(userProfile: CommonModel) {
        // Переход на профиль пользователя при нажатии на его фотографию в групповом чате
        groupUserPhoto.setOnClickListener {
            APP_ACTIVITY.replaceFragment(UserProfileFragment(userProfile))
        }
    }

    fun receivePhotoAndName(view: MessageView) {
        if (view.from != uid) {
            // Получение фотографии и имени отправителя в групповом чате
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
                        photoUrl ?: ""
                    )

                    goToProfileUser(userProfile)

                } catch (e: Exception) { }
            }
        }
    }

    override fun onAttach(view: MessageView) {
        if (view.from == uid) {
            // Обработка нажатия кнопки загрузки файла пользователя
            chatUserBtnDownload.setOnClickListener { clickToBtnFile(view) }
        } else {
            if (isGroupChat) {
                // Обработка нажатия кнопки загрузки файла в групповом чате
                groupChatReceivedBtnDownload.setOnClickListener { clickToBtnFile(view) }
            } else {
                // Обработка нажатия кнопки загрузки файла в приватном чате
                chatReceivedBtnDownload.setOnClickListener { clickToBtnFile(view) }
            }
        }
    }

    private fun clickToBtnFile(view: MessageView) {
        if (view.from == uid) {
            chatUserProgressBar.visibility = View.VISIBLE
        } else {
            if (isGroupChat) {
                groupChatReceivedProgressBar.visibility = View.VISIBLE
            } else {
                chatReceivedProgressBar.visibility = View.VISIBLE
            }
        }

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            view.text
        )

        try {
            if (checkPermission(WRITE_FILES)) {
                file.createNewFile()
                getFileFromStorage(file, view.fileUrl) {
                    if (view.from == uid) {
                        chatUserProgressBar.visibility = View.INVISIBLE
                    } else {
                        if (isGroupChat) {
                            groupChatReceivedProgressBar.visibility = View.INVISIBLE
                        } else {
                            chatReceivedProgressBar.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        } catch (e: Exception) {
            showToast("Не удалось скачать файл")
        }

    }

    override fun onDetach() {
        // Отсоединение обработчиков нажатий кнопок загрузки файла
        chatUserBtnDownload.setOnClickListener(null)
        chatReceivedBtnDownload.setOnClickListener(null)
        groupChatReceivedBtnDownload.setOnClickListener(null)
    }
}
