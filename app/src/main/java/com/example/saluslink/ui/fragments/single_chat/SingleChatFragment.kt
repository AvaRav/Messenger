package com.example.saluslink.ui.fragments.single_chat

import android.view.View
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.models.CommonModel
import com.example.saluslink.models.User
import com.example.saluslink.ui.fragments.BaseFragment
import com.example.saluslink.utilits.*
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class SingleChatFragment(private val model: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mListenerInfoHeader: AppValueEventListener
    private lateinit var mReceivingUser: User
    private lateinit var mHeaderInfo: View
    private lateinit var mRefUser: DatabaseReference
    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListener: AppChildEventListener
    private var mCountMessages = 20
    private var mIsScrolling = false
    private var mSmoothScrollToPosition = true

    override fun onResume() {
        super.onResume()
        initHeader()
        initRecycleView()
    }

    private fun initRecycleView() {
        mRecyclerView = requireView().findViewById(R.id.chat_recycler_view)
        mAdapter = SingleChatAdapter()
        mRefMessages = ref_database_root.child("messages")
            .child(uid)
            .child(model.id)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.isNestedScrollingEnabled = false
        mMessagesListener = AppChildEventListener{
            mAdapter.addItem(it.getCommonModel(), mSmoothScrollToPosition)
            if (mSmoothScrollToPosition){
                mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
            }
        }

        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    mIsScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0){
                    updateData()
                }
            }
        })
    }

    private fun updateData() {
        mSmoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages += 10
        mRefMessages.removeEventListener(mMessagesListener)
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
    }

    private fun initHeader() {
        mHeaderInfo = requireView().findViewById(R.id.message_header_block)
        mListenerInfoHeader = AppValueEventListener {
            mReceivingUser = it.getUserModel()
            initInfoHeader()
        }
        mRefUser = ref_database_root.child("users").child(model.id)
        mRefUser.addValueEventListener(mListenerInfoHeader)

        requireView().findViewById<ImageView>(R.id.chat_btn_send_message).setOnClickListener {
            mSmoothScrollToPosition = true
            val message =
                requireView().findViewById<EditText>(R.id.chat_input_message).text.toString()
            if (message.isEmpty()) {
                showToast("Введите сообщение")
            } else sendMessage(message, model.id, "text") {
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
        mRefMessages.removeEventListener(mMessagesListener)
    }
}