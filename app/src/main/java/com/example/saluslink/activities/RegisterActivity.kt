package com.example.saluslink.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.saluslink.databinding.ActivityRegisterBinding
import com.example.saluslink.utilits.initFirebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initFirebase()
    }
}