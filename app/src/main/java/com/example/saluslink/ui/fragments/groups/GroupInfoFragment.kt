package com.example.saluslink.ui.fragments.groups

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.saluslink.R
import com.example.saluslink.models.Group
import com.example.saluslink.utilits.hideKeyboard
import com.example.saluslink.viewModels.GroupInfoViewModel

class GroupInfoFragment(private val group: Group) : Fragment(R.layout.fragment_info_about_group) {
    private lateinit var viewModel: GroupInfoViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получение ссылок на элементы пользовательского интерфейса
        val nameGroup = view.findViewById<EditText>(R.id.settings_input_nameGroup)
        val direction = view.findViewById<EditText>(R.id.settings_input_direction)
        val themes = view.findViewById<EditText>(R.id.settings_input_themes)
        val aboutTheTopic = view.findViewById<EditText>(R.id.settings_input_aboutTheTopic)
        val changeAboutGroupInfoButton = view.findViewById<ImageButton>(R.id.ChangeAboutGroupButton)

        // Заполнение полей данными из объекта Group
        nameGroup.setText(group.title)
        direction.setText(group.direction)
        themes.setText(group.themes)
        aboutTheTopic.setText(group.aboutTheTopic)

        // Инициализация ViewModel
        viewModel = ViewModelProvider(this).get(GroupInfoViewModel::class.java)

        // Установка слушателя на кнопку изменения информации о группе
        changeAboutGroupInfoButton.setOnClickListener {
            val title = nameGroup.text.toString().trim()
            val directionText = direction.text.toString().trim()
            val themesText = themes.text.toString().trim()
            val aboutTheTopicText = aboutTheTopic.text.toString().trim()

            // Вызов метода изменения информации о группе в ViewModel
            viewModel.changeGroupInfo(group.id, title, directionText, themesText, aboutTheTopicText)
        }
    }

    override fun onStop() {
        super.onStop()
        // Скрытие клавиатуры при остановке фрагмента
        hideKeyboard()
    }
}
