package com.example.saluslink.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.saluslink.MainActivity
import com.example.saluslink.R
import com.example.saluslink.activities.RegisterActivity
import com.example.saluslink.utilits.*

class EnterFragment : Fragment(R.layout.fragment_enter) {
    private lateinit var password: EditText
    private lateinit var email: EditText

    override fun onStart() {
        super.onStart()

        email = requireView().findViewById<EditText>(R.id.email)
        password = requireView().findViewById<EditText>(R.id.password)

        val register = requireView().findViewById<TextView>(R.id.registration_button)
        register.setOnClickListener { register() }

        email.setOnClickListener {
            showToast("Формат адреса электронной почты: user@gmail.com")
        }

        val enterButton = requireView().findViewById<ImageButton>(R.id.EnterButton)
        enterButton.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        var emailInput = email.text.toString().trim()
        var passwordInput = password.text.toString().trim()

        if (emailInput.isEmpty() or passwordInput.isEmpty()){
            showToast("Введите адрес почты и пароль!")
        } else {
            auth.signInWithEmailAndPassword(emailInput, passwordInput)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    (activity as RegisterActivity).replaceActivity(MainActivity())
                    showToast("Добро пожаловать!")
                }
                else
                    showToast("Ошибка входа!")
            }
        }
    }

    private fun register() {
        findNavController().navigate(R.id.firstRegisterFragment)
    }
}

