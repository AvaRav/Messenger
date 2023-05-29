package com.example.saluslink.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.example.saluslink.R
import com.example.saluslink.utilits.auth
import com.example.saluslink.utilits.ref_database_root
import com.example.saluslink.utilits.replaceFragment
import com.example.saluslink.utilits.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class SecondRegisterFragment : Fragment(R.layout.fragment_second_register) {
    lateinit var surname: EditText
    lateinit var name: EditText
    lateinit var special: EditText
    lateinit var workplace: EditText
    lateinit var register: ImageButton
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

        if (surname.isEmpty() || name.isEmpty() || special.isEmpty() || workplace.isEmpty())
            showToast("Введите данные для регистрации!")
        else {
            val db = FirebaseFirestore.getInstance()
            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.let { user ->
                val userDocRef = db.collection("users").document(user.uid)
                val additionalData = hashMapOf<String, Any>(
                    "surname" to surname,
                    "name" to name,
                    "specialization" to special,
                    "workplace" to workplace
                )
                userDocRef.update(additionalData)
                    .addOnSuccessListener {

                        //добавленно мной
                        ref_database_root.child("users").child(user.uid).updateChildren(additionalData)
                            .addOnCompleteListener { task2 ->
                                if (task2.isSuccessful) {
                                    replaceFragment(EnterFragment(), false)
                                    showToast("Поздравляем с успешной регистрацией!")
                                } else showToast(task2.exception?.message.toString())
                            }
                        //


                    }
                    .addOnFailureListener {
                        showToast("Ошибка регистрации!")
                    }
            }
        }
    }
}