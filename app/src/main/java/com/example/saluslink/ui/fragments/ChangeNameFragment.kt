package com.example.saluslink.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.example.saluslink.R
import com.example.saluslink.utilits.ref_database_root
import com.example.saluslink.utilits.showToast
import com.example.saluslink.utilits.uid
import com.example.saluslink.utilits.user

class ChangeNameFragment : Fragment(R.layout.fragment_change_name) {
    override fun onResume() {
        super.onResume()

        val changeNameSurnameButton = view?.findViewById<ImageButton>(R.id.ChangeSurnameNameButton)

        changeNameSurnameButton?.setOnClickListener{
            changeNameSurname()
        }
    }

    private fun changeNameSurname() {
        val name = requireView().findViewById<EditText>(R.id.settings_input_name).text.toString().trim()
        val surname = requireView().findViewById<EditText>(R.id.settings_input_surname).text.toString().trim()

        if (name.isEmpty() && surname.isEmpty()){
            showToast(getString(R.string.fill_in_all_fields))
        } else{
            ref_database_root.child("users").child(uid).child("name").setValue(name).addOnCompleteListener{
                if (it.isSuccessful){
                    user.name = name
                    fragmentManager?.popBackStack()
                }
            }

            ref_database_root.child("users").child(uid).child("surname").setValue(surname).addOnCompleteListener {
                if (it.isSuccessful){
                    user.surname = surname
                    fragmentManager?.popBackStack()
                }
            }
        }
    }
}