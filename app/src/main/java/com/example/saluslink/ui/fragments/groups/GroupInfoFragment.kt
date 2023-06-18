package com.example.saluslink.ui.fragments.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.saluslink.R
import com.example.saluslink.models.Group
import com.example.saluslink.ui.fragments.GroupProfileFragment

import com.example.saluslink.utilits.*


class GroupInfoFragment(private val group: Group) : Fragment(R.layout.fragment_info_about_group) {
    override fun onResume() {
        super.onResume()

        val nameGroup = requireView().findViewById<EditText>(R.id.settings_input_nameGroup)
        val direction = requireView().findViewById<EditText>(R.id.settings_input_direction)
        val themes = requireView().findViewById<EditText>(R.id.settings_input_themes)
        val aboutTheTopic = requireView().findViewById<EditText>(R.id.settings_input_aboutTheTopic)
        val changeAboutGroupInfoButton = requireView().findViewById<ImageButton>(R.id.ChangeAboutGroupButton)

        nameGroup.setText(group.title)
        direction.setText(group.direction)
        themes.setText(group.themes)
        aboutTheTopic.setText(group.aboutTheTopic)

        changeAboutGroupInfoButton.setOnClickListener {
            changeGroupInfo()
        }
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    private fun changeGroupInfo() {
        val groupId = group.id
        val title = requireView().findViewById<EditText>(R.id.settings_input_nameGroup).text.toString().trim()
        val direction = requireView().findViewById<EditText>(R.id.settings_input_direction).text.toString().trim()
        val themes = requireView().findViewById<EditText>(R.id.settings_input_themes).text.toString().trim()
        val aboutTheTopic = requireView().findViewById<EditText>(R.id.settings_input_aboutTheTopic).text.toString().trim()

        if (title.isNotEmpty()) {
            ref_database_root.child("groups").child(groupId).child("title")
                .setValue(title).addOnCompleteListener {
                    if (it.isSuccessful) {
                        group.title = title
                        APP_ACTIVITY.replaceFragment(GroupProfileFragment(group))
                    }
                }
        }

        if (direction.isNotEmpty()) {
            ref_database_root.child("groups").child(groupId).child("direction")
                .setValue(direction).addOnCompleteListener {
                    if (it.isSuccessful) {
                        group.direction = direction
                        APP_ACTIVITY.replaceFragment(GroupProfileFragment(group))
                    }
                }
        }

        if (themes.isNotEmpty()) {
            ref_database_root.child("groups").child(groupId).child("themes")
                .setValue(themes).addOnCompleteListener {
                    if (it.isSuccessful) {
                        group.themes = themes
                        APP_ACTIVITY.replaceFragment(GroupProfileFragment(group))
                    }
                }
        }

        if (aboutTheTopic.isNotEmpty()) {
            ref_database_root.child("groups").child(groupId).child("aboutTheTopic")
                .setValue(aboutTheTopic).addOnCompleteListener {
                    if (it.isSuccessful) {
                        group.aboutTheTopic = aboutTheTopic
                        APP_ACTIVITY.replaceFragment(GroupProfileFragment(group))
                    }
                }
        }
    }
}