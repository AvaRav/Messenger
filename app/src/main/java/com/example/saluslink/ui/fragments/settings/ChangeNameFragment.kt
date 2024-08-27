package com.example.saluslink.ui.fragments.settings

import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ImageButton
import com.example.saluslink.R
import com.example.saluslink.utilits.*

class ChangeNameFragment : Fragment(R.layout.fragment_change_name) {
    override fun onResume() {
        super.onResume()

        // Инициализация полей ввода и кнопки
        val name = requireView().findViewById<EditText>(R.id.settings_input_name)
        val surname = requireView().findViewById<EditText>(R.id.settings_input_surname)
        val changeNameSurnameButton = view?.findViewById<ImageButton>(R.id.ChangeSurnameNameButton)

        // Установка текста в поля ввода на основе данных пользователя
        name.setText(user.name)
        surname.setText(user.surname)

        // Обработчик нажатия на кнопку изменения имени и фамилии
        changeNameSurnameButton?.setOnClickListener{
            changeNameSurname()
        }
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    private fun changeNameSurname() {
        // Получение новых значений имени и фамилии из полей ввода
        val name = requireView().findViewById<EditText>(R.id.settings_input_name).text.toString().trim()
        val surname = requireView().findViewById<EditText>(R.id.settings_input_surname).text.toString().trim()

        // Проверка, заполнены ли оба поля
        if (name.isEmpty() && surname.isEmpty()){
            showToast(getString(R.string.fill_in_all_fields))
        } else{
            // Изменение имени пользователя в базе данных
            ref_database_root.child("users").child(uid).child("name").setValue(name).addOnCompleteListener{
                if (it.isSuccessful){
                    user.name = name
                    fragmentManager?.popBackStack()
                }
            }

            // Изменение фамилии пользователя в базе данных
            ref_database_root.child("users").child(uid).child("surname").setValue(surname).addOnCompleteListener {
                if (it.isSuccessful){
                    user.surname = surname
                    APP_ACTIVITY.mAppDrawer.updateHeader()
                    fragmentManager?.popBackStack()
                }
            }

            // Изменение полного имени пользователя в базе данных
            ref_database_root.child("users").child(uid).child("fullname").setValue(name + " " + surname).addOnCompleteListener {
                if (it.isSuccessful){
                    user.fullname = name + " " + surname
                    APP_ACTIVITY.mAppDrawer.updateHeader()
                    fragmentManager?.popBackStack()
                }
            }
        }
    }
}
