package com.example.saluslink.ui.fragments.single_chat

import android.annotation.SuppressLint
import android.content.Intent
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.models.CommonModel
import com.example.saluslink.models.Group
import com.example.saluslink.ui.fragments.BaseFragment
import com.example.saluslink.ui.fragments.GroupProfileFragment
import com.example.saluslink.ui.fragments.groups.GroupsFragment
import com.example.saluslink.ui.message_recycler_view.views.AppViewFactory
import com.example.saluslink.utilits.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DatabaseReference
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView

class GroupsChatFragment(private val groups: CommonModel) : BaseFragment(R.layout.fragment_groups_chat) {

    // Поля фрагмента
    private lateinit var mListenerInfoHeader: AppValueEventListener
    private lateinit var mReceivingGroup: Group
    private lateinit var mHeaderInfo: View
    private lateinit var mRefUser: DatabaseReference
    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListener: AppChildEventListener
    private var mCountMessages = 20
    private var mSmoothScrollToPosition = true
    private lateinit var mAppVoiceRecorder: AppVoiceRecorder
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>

    override fun onResume() {
        super.onResume()
        initFields()
        initHeader()
        initRecycleView()
    }

    // Инициализация полей фрагмента
    @SuppressLint("ClickableViewAccessibility")
    private fun initFields() {
        setHasOptionsMenu(true)
        mBottomSheetBehavior = BottomSheetBehavior.from(requireView().findViewById(R.id.bottom_sheet_choice))
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        mAppVoiceRecorder = AppVoiceRecorder()
        requireView().findViewById<EditText>(R.id.group_input_message).addTextChangedListener(AppTextWatcher{
            val string = requireView().findViewById<EditText>(R.id.group_input_message).text.toString()
            if (string.isEmpty() || string == "Идет запись..."){
                requireView().findViewById<ImageView>(R.id.group_btn_send_message).visibility = View.GONE
                requireView().findViewById<ImageView>(R.id.group_btn_attach).visibility = View.VISIBLE
            } else {
                requireView().findViewById<ImageView>(R.id.group_btn_send_message).visibility = View.VISIBLE
                requireView().findViewById<ImageView>(R.id.group_btn_attach).visibility = View.GONE
            }
        })
        requireView().findViewById<ImageView>(R.id.group_btn_attach).setOnClickListener {
            attach()
        }

        requireView().findViewById<ImageView>(R.id.group_photo).setOnClickListener {
            APP_ACTIVITY.replaceFragment(GroupProfileFragment(mReceivingGroup))
        }
    }

    // Показывает всплывающую панель для выбора вложений
    private fun attach() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        requireView().findViewById<ImageView>(R.id.btn_attach_file).setOnClickListener { attachFile() }
        requireView().findViewById<ImageView>(R.id.btn_attach_image).setOnClickListener { attachImage() }
    }

    // Обработчик выбора вложения типа файл
    private fun attachFile(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    // Обработчик выбора вложения типа изображение
    private fun attachImage() {
        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(300, 300)
            .start(APP_ACTIVITY, this)
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    // Инициализация RecyclerView для отображения сообщений
    private fun initRecycleView() {
        mRecyclerView = requireView().findViewById(R.id.group_recycler_view)
        mAdapter = SingleChatAdapter(true)
        mRefMessages = ref_database_root
            .child("groups")
            .child(groups.id)
            .child("messages")
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
    }

    // Инициализация заголовка чата
    private fun initHeader() {
        mHeaderInfo = requireView().findViewById(R.id.group_message_header_block)
        mListenerInfoHeader = AppValueEventListener {
            mReceivingGroup = it.getGroupModel()
            initInfoHeader()
        }
        mRefUser = ref_database_root.child("groups").child(groups.id)
        mRefUser.addValueEventListener(mListenerInfoHeader)

        requireView().findViewById<ImageView>(R.id.group_btn_send_message).setOnClickListener {
            mSmoothScrollToPosition = true
            val message =
                requireView().findViewById<EditText>(R.id.group_input_message).text.toString()
            if (message.isEmpty()) {
                showToast("Введите сообщение")
            } else sendGroupMessage(message, groups.id, "text") {
                requireView().findViewById<EditText>(R.id.group_input_message).setText("")
            }
        }
    }

    // Инициализация информации в заголовке чата
    private fun initInfoHeader() {
        mHeaderInfo.findViewById<CircleImageView>(R.id.group_photo).downloadAndSetImage(mReceivingGroup.photoUrl)
        mHeaderInfo.findViewById<TextView>(R.id.title_group).text = mReceivingGroup.title
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data!=null){
            when (requestCode){
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val uri = CropImage.getActivityResult(data).uri
                    val messageKey = getMessageKey(groups.id)
                    uploadFileToStorageToGroup(uri, messageKey, groups.id, TYPE_MESSAGE_IMAGE)
                    mSmoothScrollToPosition = true
                }
                PICK_FILE_REQUEST_CODE -> {
                    val uri = data.data
                    val messageKey = getMessageKey(groups.id)
                    val filename = getFilenameFromUri(uri!!)
                    uploadFileToStorageToGroup(uri, messageKey, groups.id, TYPE_MESSAGE_FILE, filename)
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
        activity?.menuInflater?.inflate(R.menu.group_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_group -> deleteGroup(groups.id){
                showToast("Группа удалена")
                APP_ACTIVITY.replaceFragment(GroupsFragment())
            }
        }
        return true
    }
}
