package com.example.saluslink.ui.fragments

import android.widget.TextView
import com.example.saluslink.R
import com.example.saluslink.models.CommonModel
import com.example.saluslink.models.Group
import com.example.saluslink.utilits.downloadAndSetImageForGroup

class GroupProfileFragment(private val model: Group) : BaseFragment(R.layout.fragment_group_profile) {
    override fun onResume() {
        super.onResume()
        requireView().findViewById<TextView>(R.id.name_group).text = model.title
        requireView().findViewById<TextView>(R.id.group_profile_direction).text = model.direction
        requireView().findViewById<TextView>(R.id.group_profile_themes).text = model.themes
        requireView().findViewById<TextView>(R.id.group_profile_aboutTheTopic).text = model.aboutTheTopic
        requireView().findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.user_profile_photo)
            .downloadAndSetImageForGroup(model.photoUrl)
    }
}