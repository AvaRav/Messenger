package com.example.saluslink.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.saluslink.databinding.ActivityRegisterBinding
import com.example.saluslink.utilits.initFirebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инфлейтинг разметки активити с помощью ViewBinding
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Инициализация Firebase
        initFirebase()
    }
}
