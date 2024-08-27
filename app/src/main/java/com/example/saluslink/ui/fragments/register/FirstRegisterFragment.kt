package com.example.saluslink.ui.fragments.register

import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.saluslink.R
import com.example.saluslink.utilits.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirstRegisterFragment : Fragment(R.layout.fragment_first_register) {
    private lateinit var password: EditText
    private lateinit var email: EditText
    private lateinit var resume: ImageButton
    private lateinit var checkPassword: EditText

    override fun onStart() {
        super.onStart()

        // Инициализация полей ввода и кнопок
        email = requireView().findViewById(R.id.reg_email)
        password = requireView().findViewById(R.id.reg_password)
        resume = requireView().findViewById(R.id.FurtherButton)
        checkPassword = requireView().findViewById(R.id.re_password)

        auth = FirebaseAuth.getInstance()

        // Обработчик нажатия на поле ввода email
        email.setOnClickListener {
            showToast("Формат адреса электронной почты: user@gmail.com")
        }

        // Обработчик нажатия на поле ввода пароля
        password.setOnClickListener {
            showToast("Пароль должен содержать не менее 6 символов!")
        }

        // Обработчик нажатия на кнопку продолжения
        resume.setOnClickListener {
            signUser()
        }
    }

    private fun signUser() {
        // Получение введенных значений email и пароля
        val email = email.text.toString().trim()
        val password = password.text.toString().trim()
        val checkPassword = checkPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || checkPassword.isEmpty()) {
            showToast("Введите данные для регистрации!")
        }

        if (password != checkPassword) {
            showToast("Пароли не совпадают!")
        }

        if (email.isNotEmpty() && password.isNotEmpty() && password == checkPassword) {
            // Создание нового пользователя с помощью email и пароля
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Регистрация пользователя в Firestore базе данных
                        val db = FirebaseFirestore.getInstance()
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        currentUser?.let { user ->
                            val userDocRef = db.collection("users").document(user.uid)
                            val userData = hashMapOf<String, Any>(
                                "email" to email,
                                "password" to password
                            )
                            userDocRef.set(userData)
                        }
                        findNavController().navigate(R.id.secondRegisterFragment)
                    } else {
                        showToast("Ошибка регистрации!")
                    }
                }
        }
    }
}
