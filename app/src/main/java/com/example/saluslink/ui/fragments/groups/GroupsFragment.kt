package com.example.saluslink.ui.fragments.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.databinding.FragmentGroupsBinding
import com.example.saluslink.models.CommonModel
import com.example.saluslink.models.Group
import com.example.saluslink.ui.fragments.BaseFragment
import com.example.saluslink.utilits.*

class GroupsFragment: BaseFragment(R.layout.fragment_groups) {
    private lateinit var createGroup: Button // Кнопка для создания группы
    private lateinit var mRecyclerView: RecyclerView // RecyclerView для отображения списка групп
    private lateinit var mAdapter: GroupsAdapter // Адаптер для списка групп

    private val mRefGroups = ref_database_root.child("groups") // Ссылка на узел групп в базе данных
    private var mListGroups = listOf<CommonModel>() // Список моделей групп


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView() // Инициализация RecyclerView для списка групп

        createGroup = view.findViewById(R.id.create_group_button) // Нахождение кнопки для создания группы в макете
        createGroup.setOnClickListener {
            replaceFragment(CreateGroupFragment(), true) // Обработчик нажатия кнопки создания группы, открывает фрагмент CreateGroupFragment
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = requireView().findViewById(R.id.list_group) // Нахождение RecyclerView в макете
        mAdapter = GroupsAdapter() // Создание адаптера для списка групп

        // Загрузка списка групп из базы данных и установка его в адаптер
        mRefGroups.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
            mListGroups = dataSnapshot.children.map { it.getCommonModel() } // Преобразование данных из снимка Firebase в список моделей групп
            mListGroups.forEach { groupModel ->
                mAdapter.updateListGroup(groupModel) // Добавление каждой модели группы в адаптер
            }
            mRecyclerView.adapter = mAdapter // Установка адаптера в RecyclerView
        })
    }
}
