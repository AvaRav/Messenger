package com.example.saluslink.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.saluslink.R
import com.example.saluslink.utilits.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class SecondRegisterFragment : Fragment(R.layout.fragment_second_register) {
    private lateinit var surname: EditText
    private lateinit var name: EditText
    private lateinit var special: EditText
    private lateinit var workplace: EditText
    private lateinit var register: ImageButton

    override fun onStart() {
        super.onStart()

        surname = requireView().findViewById(R.id.reg_subname)
        name = requireView().findViewById(R.id.reg_name)
        special = requireView().findViewById(R.id.reg_specialization)
        workplace = requireView().findViewById(R.id.reg_work)
        register = requireView().findViewById(R.id.RegistrationButton)

        register.setOnClickListener {
            signUser()
        }
    }

    private fun signUser() {
        val surname = surname.text.toString().trim()
        val name = name.text.toString().trim()
        val special = special.text.toString().trim()
        val workplace = workplace.text.toString().trim()
        var fullname = name + " " + surname

        val education = getString(R.string.not_specified)
        val experience = getString(R.string.not_specified)

        if (surname.isEmpty() || name.isEmpty() || special.isEmpty() || workplace.isEmpty())
            showToast("Введите данные для регистрации!")
        else {
            val db = FirebaseFirestore.getInstance()
            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.let { user ->
                val userDocRef = db.collection("users").document(user.uid)
                val additionalData = hashMapOf<String, Any>(
                    "id" to user.uid,
                    "surname" to surname,
                    "name" to name,
                    "fullname" to fullname,
                    "specialization" to special,
                    "workplace" to workplace,
                    "education" to education,
                    "experience" to experience,

                )
                userDocRef.update(additionalData)
                    .addOnSuccessListener {

                        ref_database_root.child("users").child(user.uid).updateChildren(additionalData)
                            .addOnCompleteListener { task2 ->
                                if (task2.isSuccessful) {
                                    findNavController().navigate(R.id.enterFragment)
                                    showToast("Поздравляем с успешной регистрацией!")
                                } else showToast(task2.exception?.message.toString())
                            }

                    }
                    .addOnFailureListener {
                        showToast("Ошибка регистрации!")
                    }
            }
        }
    }
}