package com.example.saluslink.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.saluslink.MainActivity
import com.example.saluslink.R
import com.example.saluslink.activities.RegisterActivity
import com.example.saluslink.utilits.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)

        val name = requireView().findViewById<TextView>(R.id.settings_name)
        val change_name = requireView().findViewById<TextView>(R.id.settings_fio)
        val status = requireView().findViewById<TextView>(R.id.settings_text_online_offline)
        val changeNameButton = view?.findViewById<ConstraintLayout>(R.id.settings_btn_change_fio)
        val changeAboutButton = view?.findViewById<ConstraintLayout>(R.id.settings_btn_change_about)
        val ChangePhotoButton = requireView().findViewById<ConstraintLayout>(R.id.settings_btn_change_photo)

        name.text = user.name
        change_name.text = user.name
        status.text = user.status

        ChangePhotoButton?.setOnClickListener{
            changeUserPhoto()
        }

        changeNameButton?.setOnClickListener{
            replaceFragment(ChangeNameFragment(), true)
        }

        changeAboutButton?.setOnClickListener{
            replaceFragment(AboutFragment(), true)
        }
    }

    private fun changeUserPhoto() {
        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                auth.signOut()
                (APP_ACTIVITY).replaceActivity(RegisterActivity())
            }
        }
        return true
    }
}