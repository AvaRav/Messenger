package com.example.saluslink.ui.fragments

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.saluslink.MainActivity
import com.example.saluslink.R
import com.example.saluslink.activities.RegisterActivity
import com.example.saluslink.utilits.auth
import com.example.saluslink.utilits.replaceActivity
import com.example.saluslink.utilits.replaceFragment

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)

        val changeNameButton = view?.findViewById<ConstraintLayout>(R.id.settings_btn_change_fio)
        val changeAboutButton = view?.findViewById<ConstraintLayout>(R.id.settings_btn_change_about)

        changeNameButton?.setOnClickListener{
            replaceFragment(ChangeNameFragment(), true)
        }

        changeAboutButton?.setOnClickListener{
            replaceFragment(AboutFragment(), true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                auth.signOut()
                (activity as MainActivity).replaceActivity(RegisterActivity())
            }
        }
        return true
    }
}