package com.example.saluslink.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.models.CommonModel
import com.example.saluslink.ui.fragments.single_chat.SingleChatFragment
import com.example.saluslink.utilits.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import de.hdodenhof.circleimageview.CircleImageView

class UsersFragment : BaseFragment(R.layout.fragment_users) {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, UsersHolder>
    private lateinit var mRefFriends: DatabaseReference

    override fun onResume() {
        super.onResume()
        initRecycleView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Настройка функционала поиска
        val searchEditText = view.findViewById<EditText>(R.id.search_edit_text)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                if (searchText.isEmpty()) {
                    // Если поле поиска пустое, запросить всех пользователей по их статусу
                    val query = ref_database_root.child("users")
                        .orderByChild("status")
                        .limitToFirst(30)
                    updateAdapter(query)
                } else {
                    // Если есть текст поиска, запросить пользователей по их полному имени (в будущем изменить на полное имя)
                    val query = ref_database_root.child("users")
                        .orderByChild("fullname")
                        .startAt(searchText)
                        .endAt(searchText + "\uf8ff")
                    updateAdapter(query)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateAdapter(query: Query) {
        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(query, CommonModel::class.java)
            .build()

        // Обновление адаптера с новыми опциями запроса
        mAdapter.updateOptions(options)
        mAdapter.notifyDataSetChanged()
    }

    private fun initRecycleView() {
        mRecyclerView = requireView().findViewById(R.id.friend_recycle_view)
        mRefFriends = ref_database_root.child("users").child(uid)

        val defaultQuery = ref_database_root.child("users")
            .orderByChild("status")
            .limitToFirst(30)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(defaultQuery, CommonModel::class.java)
            .build()

        mAdapter = object : FirebaseRecyclerAdapter<CommonModel, UsersHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersHolder {
                // Создание макета user_item для каждого элемента в RecyclerView
                val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
                return UsersHolder(view)
            }

            @SuppressLint("SetTextI18n")
            override fun onBindViewHolder(holder: UsersHolder, position: Int, model: CommonModel) {
                // Привязка данных к ViewHolder
                holder.name.text = model.name + " " + model.surname
                holder.status.text = model.status
                holder.photo.downloadAndSetImage(model.photoUrl)

                // Установка слушателя нажатия на элемент для открытия SingleChatFragment
                holder.itemView.setOnClickListener{
                    APP_ACTIVITY.replaceFragment(SingleChatFragment(model))
                }
            }

            override fun onError(databaseError: DatabaseError) {
                Log.e("FirebaseRecyclerAdapter", "Произошла ошибка: ${databaseError.message}")
            }
        }

        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }

    class UsersHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Класс ViewHolder для кэширования ссылок на виджеты
        val name: TextView = view.findViewById(R.id.friend_name)
        val status: TextView = view.findViewById(R.id.status)
        val photo: CircleImageView = view.findViewById(R.id.friend_photo)
    }

    override fun onPause() {
        super.onPause()
        // Прекратить прослушивание обновлений FirebaseRecyclerAdapter
        mAdapter.stopListening()
    }
}
