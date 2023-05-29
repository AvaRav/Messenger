package com.example.saluslink.ui.fragments

import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.saluslink.MainActivity
import com.example.saluslink.R
import com.example.saluslink.activities.RegisterActivity
import com.example.saluslink.utilits.auth
import com.example.saluslink.utilits.replaceActivity
import com.example.saluslink.utilits.replaceFragment
import com.example.saluslink.utilits.showToast

class EnterFragment : Fragment(R.layout.fragment_enter) {
    lateinit var password: EditText
    lateinit var email: EditText
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
        var email = email.text.toString().trim()
        var password = password.text.toString().trim()

        if (email.isEmpty() or password.isEmpty()){
            showToast("Введите адрес почты и пароль!")
        } else {
            auth.signInWithEmailAndPassword(email, password)
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
        replaceFragment(FirstRegisterFragment(),false)
    }


}