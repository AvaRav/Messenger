package com.example.saluslink.ui.fragments

import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.saluslink.R
import com.example.saluslink.utilits.replaceFragment
import com.example.saluslink.utilits.showToast

class EnterFragment : Fragment(R.layout.fragment_enter) {
    override fun onStart() {
        super.onStart()

        val enterButton = requireView().findViewById<ImageButton>(R.id.EnterButton)
        enterButton.setOnClickListener { sendCode() }

        val register = requireView().findViewById<TextView>(R.id.registration_button)
        register.setOnClickListener { register() }
    }

    private fun sendCode() {
        val phone = requireView().findViewById<EditText>(R.id.phone)
        val password = requireView().findViewById<EditText>(R.id.password)

        if (phone.text.toString().isEmpty() or password.text.toString().isEmpty()){
            showToast("Введите номер телефона и пароль!")
        } else {
            showToast("ОК")
        }
    }

    private fun register() {
        replaceFragment(FirstRegisterFragment(),false)
    }


}