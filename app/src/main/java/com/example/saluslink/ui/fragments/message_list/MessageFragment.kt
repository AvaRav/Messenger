package com.example.saluslink.ui.fragments.message_list

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.models.CommonModel
import com.example.saluslink.ui.fragments.BaseFragment
import com.example.saluslink.utilits.*

class MessageFragment : BaseFragment(R.layout.fragment_message) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MessageAdapter

    private val mRefMessageList = ref_database_root.child("message_list").child(uid)
    private val mRefUser = ref_database_root.child("users")
    private val mRefMessages = ref_database_root.child("messages").child(uid)
    private var mListItems = listOf<CommonModel>()

    override fun onResume() {
        super.onResume()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        // Инициализация RecyclerView для отображения списка сообщений
        mRecyclerView = requireView().findViewById(R.id.message_list_recycle_view)
        mAdapter = MessageAdapter()

        // Получение данных из Firebase и заполнение списка сообщений
        mRefMessageList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
            val tempList = dataSnapshot.children.map { it.getCommonModel() }
            mListItems = tempList.filter { it.type == "chat" }

            mListItems.forEach { chatModel ->
                // Получение данных о пользователе для отображения в списке
                mRefUser.child(chatModel.id).addListenerForSingleValueEvent(AppValueEventListener { userSnapshot ->
                    val newModel = userSnapshot.getCommonModel()

                    // Получение последнего сообщения в чате для отображения в списке
                    mRefMessages.child(chatModel.id).limitToLast(1).addListenerForSingleValueEvent(AppValueEventListener { messagesSnapshot ->
                        val messagesList = messagesSnapshot.children.map { it.getCommonModel() }

                        // Если в чате нет сообщений, отображается текст "Чат очищен", иначе отображается последнее сообщение
                        if (messagesList.isEmpty()) {
                            newModel.lastMessage = "Чат очищен"
                        } else {
                            newModel.lastMessage = messagesList[0].text
                        }

                        // Проверяем наличие первого сообщения в диалоге
                        if (mListItems.isEmpty()) {
                            // Показываем иконку
                            requireView().findViewById<ImageView>(R.id.iconApplication).visibility = View.VISIBLE
                        } else {
                            // Скрываем иконку
                            requireView().findViewById<ImageView>(R.id.iconApplication).visibility = View.GONE
                        }

                        // Обновление списка сообщений в адаптере
                        mAdapter.updateListItems(newModel)
                    })
                })
            }

            // Установка адаптера для RecyclerView
            mRecyclerView.adapter = mAdapter
        })
    }
}
