package com.example.saluslink.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.example.saluslink.R
import com.example.saluslink.utilits.replaceFragment
import com.example.saluslink.utilits.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirstRegisterFragment : Fragment(R.layout.fragment_first_register) {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = requireView().findViewById<EditText>(R.id.reg_phone)
        val password = requireView().findViewById<EditText>(R.id.reg_password)
        val register = requireView().findViewById<ImageButton>(R.id.FurtherButton)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        register.setOnClickListener {
            val email = email.text.toString().trim()
            val password = password.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            database.child("Users").child("email").setValue(email)
                            database.child("Users").child("password").setValue(password)
                                .addOnCompleteListener { databaseTask ->
                                    if (databaseTask.isSuccessful) {
                                        replaceFragment(SecondRegisterFragment(),false)
                                    } else {
                                        showToast("Ошибка!")
                                    }
                                }
                        } else {
                            showToast("Аккаунт уже существует!")
                        }
                    }
            } else {
                showToast("Введите данные для регистрации!")
            }
        }
    }
}