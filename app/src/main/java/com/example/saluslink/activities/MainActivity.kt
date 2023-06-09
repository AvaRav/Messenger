package com.example.saluslink.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.saluslink.databinding.ActivityMainBinding
import com.example.saluslink.ui.fragments.message_list.MessageFragment
import com.example.saluslink.ui.objects.AppDrawer
import com.example.saluslink.utilits.*

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    lateinit var mAppDrawer: AppDrawer
    lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFirebase()
        initUser{
            initFields()
            initFunc()
        }
    }

    private fun initFunc() {
        if (auth.currentUser != null) {
            setSupportActionBar(mToolbar)
            mAppDrawer.create()
            replaceFragment(MessageFragment(), false)
        } else {
            replaceActivity(RegisterActivity())
        }
    }


    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            AppStates.updateState(currentUser.uid, AppStates.ONLINE)
        }
    }

    override fun onStop() {
        super.onStop()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            AppStates.updateState(currentUser.uid, AppStates.OFFLINE)
        }
    }
}