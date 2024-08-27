package com.example.saluslink.ui.fragments.single_chat

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.*
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.activities.RegisterActivity
import com.example.saluslink.models.CommonModel
import com.example.saluslink.models.User
import com.example.saluslink.ui.fragments.BaseFragment
import com.example.saluslink.ui.fragments.UserProfileFragment
import com.example.saluslink.ui.fragments.message_list.MessageAdapter
import com.example.saluslink.ui.fragments.message_list.MessageFragment
import com.example.saluslink.ui.message_recycler_view.views.AppViewFactory
import com.example.saluslink.utilits.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DatabaseReference
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleChatFragment(private val model: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {

    // Объявление переменных
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
    private lateinit var mAppVoiceRecorder: AppVoiceRecorder
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>

    // Этот метод вызывается при возобновлении фрагмента
    override fun onResume() {
        super.onResume()
        initFields()
        initHeader()
        initRecycleView()
    }

    // Инициализация полей представлений и установка слушателей
    @SuppressLint("ClickableViewAccessibility")
    private fun initFields() {
        setHasOptionsMenu(true)
        mBottomSheetBehavior = BottomSheetBehavior.from(requireView().findViewById(R.id.bottom_sheet_choice))
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        mAppVoiceRecorder = AppVoiceRecorder()

        // Установка слушателя изменения текста для поля ввода сообщения
        requireView().findViewById<EditText>(R.id.chat_input_message).addTextChangedListener(AppTextWatcher{
            val string = requireView().findViewById<EditText>(R.id.chat_input_message).text.toString()

            // Если сообщение пустое или равно "Идет запись..." (идет запись голоса), скрыть/отобразить кнопки
            if (string.isEmpty() || string == "Идет запись..."){
                requireView().findViewById<ImageView>(R.id.chat_btn_send_message).visibility = View.GONE
                requireView().findViewById<ImageView>(R.id.chat_btn_attach).visibility = View.VISIBLE
                requireView().findViewById<ImageView>(R.id.chat_btn_voice).visibility = View.VISIBLE
            } else {
                requireView().findViewById<ImageView>(R.id.chat_btn_send_message).visibility = View.VISIBLE
                requireView().findViewById<ImageView>(R.id.chat_btn_attach).visibility = View.GONE
                requireView().findViewById<ImageView>(R.id.chat_btn_voice).visibility = View.GONE
            }
        })

        // Установка слушателя нажатия для кнопки прикрепления
        requireView().findViewById<ImageView>(R.id.chat_btn_attach).setOnClickListener {
            // Логика обработки нажатия кнопки прикрепления
            attach()
        }

        CoroutineScope(Dispatchers.IO).launch {
            // Установка слушателя касания для кнопки голосового сообщения
            requireView().findViewById<ImageView>(R.id.chat_btn_voice).setOnTouchListener { v, event ->
                if (checkPermission(RECORD_AUDIO)){
                    if (event.action == MotionEvent.ACTION_DOWN){
                        requireView().findViewById<EditText>(R.id.chat_input_message).setText("Идет запись...")
                        requireView().findViewById<ImageView>(R.id.chat_btn_voice)
                            .setColorFilter(ContextCompat.getColor(APP_ACTIVITY, R.color.colorPrimaryVariantDark))
                        val messageKey = getMessageKey(model.id)
                        mAppVoiceRecorder.startRecord(messageKey)
                    } else if(event.action == MotionEvent.ACTION_UP){
                        requireView().findViewById<EditText>(R.id.chat_input_message).setText("")
                        requireView().findViewById<ImageView>(R.id.chat_btn_voice)
                            .colorFilter = null
                        mAppVoiceRecorder.stopRecord{ file, messageKey ->
                            uploadFileToStorage(Uri.fromFile(file), messageKey, model.id, TYPE_MESSAGE_VOICE)
                            mSmoothScrollToPosition = true
                        }
                    }
                }
                true
            }
        }

        // Установка слушателя нажатия для фотографии пользователя
        requireView().findViewById<ImageView>(R.id.user_photo).setOnClickListener {
            APP_ACTIVITY.replaceFragment(UserProfileFragment(model))
        }
    }

    // Обработка нажатия кнопки прикрепления
    private fun attach() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        // Установка слушателя нажатия для кнопки прикрепления файла
        requireView().findViewById<ImageView>(R.id.btn_attach_file).setOnClickListener { attachFile() }
        // Установка слушателя нажатия для кнопки прикрепления изображения
        requireView().findViewById<ImageView>(R.id.btn_attach_image).setOnClickListener { attachImage() }
    }

    // Обработка прикрепления файла
    private fun attachFile(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    // Обработка прикрепления изображения
    private fun attachImage() {
        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(300, 300)
            .start(APP_ACTIVITY, this)
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }


    // Инициализация RecyclerView и адаптера
    private fun initRecycleView() {
        mRecyclerView = requireView().findViewById(R.id.chat_recycler_view)
        mAdapter = SingleChatAdapter(false)
        mRefMessages = ref_database_root.child("messages")
            .child(uid)
            .child(model.id)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.isNestedScrollingEnabled = false
        mMessagesListener = AppChildEventListener{
            mAdapter.addItem(AppViewFactory.getView(it.getCommonModel()), mSmoothScrollToPosition)
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

    // Обновление данных RecyclerView при прокрутке вверх
    private fun updateData() {
        mSmoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages += 10
        mRefMessages.removeEventListener(mMessagesListener)
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
    }

    // Инициализация информационного заголовка
    private fun initHeader() {
        mHeaderInfo = requireView().findViewById(R.id.message_header_block)
        mListenerInfoHeader = AppValueEventListener {
            mReceivingUser = it.getUserModel()
            initInfoHeader()
        }
        mRefUser = ref_database_root.child("users").child(model.id)
        mRefUser.addValueEventListener(mListenerInfoHeader)

        // Установка слушателя нажатия для кнопки отправки сообщения
        requireView().findViewById<ImageView>(R.id.chat_btn_send_message).setOnClickListener {
            mSmoothScrollToPosition = true
            val message =
                requireView().findViewById<EditText>(R.id.chat_input_message).text.toString()
            if (message.isEmpty()) {
                showToast("Введите сообщение")
            } else sendMessage(message, model.id, "text") {
                saveToMessageList(model.id, TYPE_CHAT)
                requireView().findViewById<EditText>(R.id.chat_input_message).setText("")
            }
        }
    }

    // Инициализация информационного заголовка чата
    private fun initInfoHeader() {
        mHeaderInfo.findViewById<ImageView>(R.id.user_photo).downloadAndSetImage(mReceivingUser.photoUrl)
        mHeaderInfo.findViewById<TextView>(R.id.user_name).text = mReceivingUser.fullname
        mHeaderInfo.findViewById<TextView>(R.id.message_text_online_offline).text = mReceivingUser.status
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data!=null){
            when (requestCode){
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val uri = CropImage.getActivityResult(data).uri
                    val messageKey = getMessageKey(model.id)
                    uploadFileToStorage(uri, messageKey,  model.id, TYPE_MESSAGE_IMAGE)
                    mSmoothScrollToPosition = true
                }
                PICK_FILE_REQUEST_CODE -> {
                    val uri = data.data
                    val messageKey = getMessageKey(model.id)
                    val filename = getFilenameFromUri(uri!!)
                    uploadFileToStorage(uri, messageKey, model.id, TYPE_MESSAGE_FILE, filename)
                    mSmoothScrollToPosition = true
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mRefUser.removeEventListener(mListenerInfoHeader)
        mRefMessages.removeEventListener(mMessagesListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        mAppVoiceRecorder.releaseRecorder()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.single_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_clear_chat -> clearChat(model.id){
                showToast("Чат очищен")
                APP_ACTIVITY.replaceFragment(MessageFragment())
            }
            R.id.menu_delete_chat -> deleteChat(model.id){
                showToast("Чат удален")
                APP_ACTIVITY.replaceFragment(MessageFragment())
            }
        }
        return true
    }
}