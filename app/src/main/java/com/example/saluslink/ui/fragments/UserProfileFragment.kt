package com.example.saluslink.ui.fragments

import android.widget.TextView
import com.example.saluslink.R
import com.example.saluslink.models.CommonModel
import com.example.saluslink.utilits.downloadAndSetImage

class UserProfileFragment(private val model: CommonModel) : BaseFragment(R.layout.fragment_user_profile) {
    override fun onResume() {
        super.onResume()
        requireView().findViewById<TextView>(R.id.user_profile_name).text = model.name
        requireView().findViewById<TextView>(R.id.user_profile_surname).text = model.surname
        requireView().findViewById<TextView>(R.id.user_profile_specialization).text = model.specialization
        requireView().findViewById<TextView>(R.id.user_profile_education).text = model.education
        requireView().findViewById<TextView>(R.id.user_profile_workplace).text = model.workplace
        requireView().findViewById<TextView>(R.id.user_profile_experience).text = model.experience
        requireView().findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.user_profile_photo)
            .downloadAndSetImage(model.photoUrl)
    }
}