package com.example.saluslink.ui.message_recycler_view.view_holders

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.ui.message_recycler_view.views.MessageView
import com.example.saluslink.utilits.*
import java.io.File

class HolderFileMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolderInterface{

    private val blockReceivedFile: ConstraintLayout = view.findViewById(R.id.block_received_file_message)
    private val blockUserFile: ConstraintLayout = view.findViewById(R.id.block_user_file_message)
    private val chatUserFileMessageTime: TextView = view.findViewById(R.id.chat_user_file_message_time)
    private val chatReceivedFileMessageTime: TextView = view.findViewById(R.id.chat_received_file_message_time)

    private val chatUserFilename: TextView = view.findViewById(R.id.chat_user_file_name)
    private val chatReceivedFilename: TextView = view.findViewById(R.id.chat_received_file_name)

    private val chatUserBtnDownload: ImageView = view.findViewById(R.id.chat_user_btn_download)
    private val chatReceivedBtnDownload: ImageView = view.findViewById(R.id.chat_received_btn_download)

    private val chatUserProgressBar: ProgressBar = view.findViewById(R.id.user_progressBar)
    private val chatReceivedProgressBar: ProgressBar = view.findViewById(R.id.received_progressBar)

    override fun drawMessage(view: MessageView) {
        if (view.from == uid){
            blockReceivedFile.visibility = View.GONE
            blockUserFile.visibility = View.VISIBLE
            chatUserFileMessageTime.text = view.timeStamp.asTime()
            chatUserFilename.text = view.text
        } else{
            blockReceivedFile.visibility = View.VISIBLE
            blockUserFile.visibility = View.GONE
            chatReceivedFileMessageTime.text = view.timeStamp.asTime()
            chatReceivedFilename.text = view.text
        }
    }

    override fun onAttach(view: MessageView) {
        if (view.from == uid) chatUserBtnDownload.setOnClickListener { clickToBtnFile(view) }
        else chatReceivedBtnDownload.setOnClickListener { clickToBtnFile(view)  }
    }

    private fun clickToBtnFile(view: MessageView) {
        if (view.from == uid){
            chatUserProgressBar.visibility = View.VISIBLE
        } else {
            chatReceivedProgressBar.visibility = View.VISIBLE
        }

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            view.text
        )

        try {
            if (checkPermission(WRITE_FILES)){
                file.createNewFile()
                getFileFromStorage(file, view.fileUrl){
                    if (view.from == uid){
                        chatUserProgressBar.visibility = View.INVISIBLE
                    } else {
                        chatReceivedProgressBar.visibility = View.INVISIBLE
                    }
                }
            }
        } catch (e: Exception){
            showToast("Не удалось скачать файл")
        }

    }

    override fun onDetach() {
        chatUserBtnDownload.setOnClickListener(null)
        chatReceivedBtnDownload.setOnClickListener(null)

    }
}