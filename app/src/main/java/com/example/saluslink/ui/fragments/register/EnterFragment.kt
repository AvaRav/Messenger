package com.example.saluslink.ui.fragments.register

import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.saluslink.activities.MainActivity
import com.example.saluslink.R
import com.example.saluslink.activities.RegisterActivity
import com.example.saluslink.utilits.*

class EnterFragment : Fragment(R.layout.fragment_enter) {
    private lateinit var password: EditText
    private lateinit var email: EditText
    private lateinit var info: ImageView

    override fun onStart() {
        super.onStart()

        // Инициализация полей ввода и кнопок
        email = requireView().findViewById<EditText>(R.id.email)
        password = requireView().findViewById<EditText>(R.id.password)
        info = requireView().findViewById(R.id.info)

        val register = requireView().findViewById<TextView>(R.id.registration_button)
        register.setOnClickListener { register() }

        // Обработчик нажатия на кнопку информации
        info.setOnClickListener {
            findNavController().navigate(R.id.infoFragment)
        }

        // Обработчик нажатия на поле ввода email
        email.setOnClickListener {
            showToast("Формат адреса электронной почты: user@gmail.com")
        }

        val enterButton = requireView().findViewById<ImageButton>(R.id.EnterButton)
        enterButton.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        // Получение введенных значений email и пароля
        var emailInput = email.text.toString().trim()
        var passwordInput = password.text.toString().trim()

        if (emailInput.isEmpty() or passwordInput.isEmpty()) {
            showToast("Введите адрес почты и пароль!")
        } else {
            // Вход пользователя с помощью email и пароля
            auth.signInWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Переход к основному экрану после успешного входа
                        (activity as RegisterActivity).replaceActivity(MainActivity())
                        showToast("Добро пожаловать!")
                    } else {
                        showToast("Ошибка входа!")
                    }
                }
        }
    }

    private fun register() {
        // Переход к фрагменту регистрации
        findNavController().navigate(R.id.firstRegisterFragment)
    }
}
