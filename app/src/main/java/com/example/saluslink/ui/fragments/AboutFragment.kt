package com.example.saluslink.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.example.saluslink.R
import com.example.saluslink.utilits.ref_database_root
import com.example.saluslink.utilits.showToast
import com.example.saluslink.utilits.uid
import com.example.saluslink.utilits.user

class AboutFragment : Fragment(R.layout.fragment_about) {
    override fun onResume() {
        super.onResume()

        val ChangeAboutButton = view?.findViewById<ImageButton>(R.id.ChangeAboutButton)

        ChangeAboutButton?.setOnClickListener {
            changeAbout()
        }
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