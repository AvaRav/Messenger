package com.example.saluslink.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.example.saluslink.R
import com.example.saluslink.utilits.*

class AboutFragment : Fragment(R.layout.fragment_about) {
    override fun onResume() {
        super.onResume()

        val specialization = requireView().findViewById<EditText>(R.id.settings_input_specialization)
        val education = requireView().findViewById<EditText>(R.id.settings_input_education)
        val workspace = requireView().findViewById<EditText>(R.id.settings_input_working)
        val experience = requireView().findViewById<EditText>(R.id.settings_input_experience)
        val ChangeAboutButton = view?.findViewById<ImageButton>(R.id.ChangeAboutButton)

        specialization.setText(user.specialization)
        education.setText(user.education)
        workspace.setText(user.workplace)
        experience.setText(user.experience)

        ChangeAboutButton?.setOnClickListener {
            changeAbout()
        }
    }

    override fun onStop() {
        super.onStop()
        APP_ACTIVITY.hideKeyboard()
    }

    private fun changeAbout() {
        val specialization =
            requireView().findViewById<EditText>(R.id.settings_input_specialization).text.toString()
                .trim()
        val education =
            requireView().findViewById<EditText>(R.id.settings_input_education).text.toString()
                .trim()
        val workplace =
            requireView().findViewById<EditText>(R.id.settings_input_working).text.toString().trim()
        val experience =
            requireView().findViewById<EditText>(R.id.settings_input_experience).text.toString()
                .trim()

        if (specialization.isNotEmpty()) {
            ref_database_root.child("users").child(uid).child("specialization")
                .setValue(specialization).addOnCompleteListener {
                if (it.isSuccessful) {
                    user.specialization = specialization
                }
            }
        }

        if (education.isNotEmpty()) {
            ref_database_root.child("users").child(uid).child("education")
                .setValue(education).addOnCompleteListener {
                    if (it.isSuccessful) {
                        user.education = education
                    }
                }
        }

        if (education.isNotEmpty()) {
            ref_database_root.child("users").child(uid).child("workplace")
                .setValue(workplace).addOnCompleteListener {
                    if (it.isSuccessful) {
                        user.workplace = workplace
                    }
                }
        }

        if (education.isNotEmpty()) {
            ref_database_root.child("users").child(uid).child("experience")
                .setValue(experience).addOnCompleteListener {
                    if (it.isSuccessful) {
                        user.experience = experience
                    }
                }
        }

        fragmentManager?.popBackStack()
    }
}