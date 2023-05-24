package com.example.saluslink.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.saluslink.R
import com.example.saluslink.databinding.ActivityRegisterBinding
import com.example.saluslink.ui.fragments.EnterFragment
import com.example.saluslink.utilits.replaceFragment

class RegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        replaceFragment(EnterFragment())
    }
}