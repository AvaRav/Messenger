package com.example.saluslink.ui.fragments.settings

import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ImageButton
import com.example.saluslink.R
import com.example.saluslink.utilits.*

class ChangeAboutFragment : Fragment(R.layout.fragment_about) {
    override fun onResume() {
        super.onResume()

        // Инициализация полей ввода и кнопки
        val specialization = requireView().findViewById<EditText>(R.id.settings_input_specialization)
        val education = requireView().findViewById<EditText>(R.id.settings_input_education)
        val workspace = requireView().findViewById<EditText>(R.id.settings_input_working)
        val experience = requireView().findViewById<EditText>(R.id.settings_input_experience)
        val ChangeAboutButton = view?.findViewById<ImageButton>(R.id.ChangeAboutButton)

        // Установка текста в поля ввода на основе данных пользователя
        specialization.setText(user.specialization)
        education.setText(user.education)
        workspace.setText(user.workplace)
        experience.setText(user.experience)

        // Обработчик нажатия на кнопку изменения информации
        ChangeAboutButton?.setOnClickListener {
            changeAbout()
        }
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    private fun changeAbout() {
        // Получение новых значений из полей ввода
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

        // Изменение специализации в базе данных
        if (specialization.isNotEmpty()) {
            ref_database_root.child("users").child(uid).child("specialization")
                .setValue(specialization).addOnCompleteListener {
                    if (it.isSuccessful) {
                        user.specialization = specialization
                    }
                }
        }

        // Изменение образования в базе данных
        if (education.isNotEmpty()) {
            ref_database_root.child("users").child(uid).child("education")
                .setValue(education).addOnCompleteListener {
                    if (it.isSuccessful) {
                        user.education = education
                    }
                }
        }

        // Изменение места работы в базе данных
        if (workplace.isNotEmpty()) {
            ref_database_root.child("users").child(uid).child("workplace")
                .setValue(workplace).addOnCompleteListener {
                    if (it.isSuccessful) {
                        user.workplace = workplace
                    }
                }
        }

        // Изменение опыта работы в базе данных
        if (experience.isNotEmpty()) {
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
