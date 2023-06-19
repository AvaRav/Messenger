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

        val nameGroup = view.findViewById<EditText>(R.id.settings_input_nameGroup)
        val direction = view.findViewById<EditText>(R.id.settings_input_direction)
        val themes = view.findViewById<EditText>(R.id.settings_input_themes)
        val aboutTheTopic = view.findViewById<EditText>(R.id.settings_input_aboutTheTopic)
        val changeAboutGroupInfoButton = view.findViewById<ImageButton>(R.id.ChangeAboutGroupButton)

        nameGroup.setText(group.title)
        direction.setText(group.direction)
        themes.setText(group.themes)
        aboutTheTopic.setText(group.aboutTheTopic)

        viewModel = ViewModelProvider(this).get(GroupInfoViewModel::class.java)

        changeAboutGroupInfoButton.setOnClickListener {
            val title = nameGroup.text.toString().trim()
            val directionText = direction.text.toString().trim()
            val themesText = themes.text.toString().trim()
            val aboutTheTopicText = aboutTheTopic.text.toString().trim()

            viewModel.changeGroupInfo(group.id, title, directionText, themesText, aboutTheTopicText)
        }
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }
}