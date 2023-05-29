package com.example.saluslink.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ImageButton
import com.example.saluslink.R
import com.example.saluslink.utilits.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class FirstRegisterFragment : Fragment(R.layout.fragment_first_register) {
    lateinit var password: EditText
    lateinit var email: EditText
    lateinit var resume: ImageButton
    lateinit var checkPassword: EditText

    override fun onStart() {
        super.onStart()

        email = requireView().findViewById(R.id.reg_email)
        password = requireView().findViewById(R.id.reg_password)
        resume = requireView().findViewById(R.id.FurtherButton)
        checkPassword = requireView().findViewById(R.id.re_password)

        auth = FirebaseAuth.getInstance()

        email.setOnClickListener {
            showToast("Формат адреса электронной почты: user@gmail.com")
        }

        password.setOnClickListener {
            showToast("Пароль должен содержать не менее 6 символов!")
        }

        resume.setOnClickListener {
            signUser()
        }
    }

    private fun signUser() {
        val email = email.text.toString().trim()
        val password = password.text.toString().trim()
        val checkPassword = checkPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || checkPassword.isEmpty())
            showToast("Введите данные для регистрации!")


        if (password != checkPassword)
            showToast("Пароли не совпадают!")

        if (email.isNotEmpty() && password.isNotEmpty() && password == checkPassword)
        {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
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
                        replaceFragment(SecondRegisterFragment(), false)
                    } else {
                        showToast("Ошибка регистрации!")
                    }
                }
        }
    }
}