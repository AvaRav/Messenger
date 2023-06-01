package com.example.saluslink.ui.fragments

import android.widget.TextView
import com.example.saluslink.R
import com.example.saluslink.databinding.FragmentProfileBinding
import com.example.saluslink.utilits.downloadAndSetImage
import com.example.saluslink.utilits.user

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private lateinit var mBinding: FragmentProfileBinding

    override fun onResume() {
        super.onResume()

        //В случае чего переписать под DataBinding

        val name = requireView().findViewById<TextView>(R.id.name)
        val surname = requireView().findViewById<TextView>(R.id.surname)
        val photo = requireView().findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.user_photo)
        val specialization = requireView().findViewById<TextView>(R.id.profile_specialization)
        val education = requireView().findViewById<TextView>(R.id.profile_education)
        val workplace = requireView().findViewById<TextView>(R.id.profile_workplace)
        val experience = requireView().findViewById<TextView>(R.id.profile_experience)

        name.text = user.name
        surname.text = user.surname
        specialization.text = user.specialization
        education.text = user.education
        workplace.text = user.workplace
        experience.text = user.experience
        photo.downloadAndSetImage(user.photoUrl)
    }
}