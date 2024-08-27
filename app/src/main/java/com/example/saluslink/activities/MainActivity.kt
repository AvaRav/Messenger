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

        // Инфлейтинг разметки активити с помощью ViewBinding
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Установка текущей активити в качестве основной активити приложения
        APP_ACTIVITY = this

        // Инициализация Firebase
        initFirebase()

        // Инициализация пользователя и выполнение действий после успешной инициализации
        initUser{
            initFields()
            initFunc()
        }
    }

    // Функция инициализации функциональных частей активити
    private fun initFunc() {
        // Если пользователь аутентифицирован, то устанавливаем Toolbar,
        // создаем AppDrawer (боковое меню) и заменяем фрагмент на экране
        if (auth.currentUser != null && user.name!="") {
            setSupportActionBar(mToolbar)
            mAppDrawer.create()
            replaceFragment(MessageFragment(), false)
        } else {
            // Если пользователь не аутентифицирован, переходим на активити регистрации
            replaceActivity(RegisterActivity())
        }
    }

    // Функция инициализации полей активити
    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer()
    }

    // При старте активити проверяем, аутентифицирован ли текущий пользователь
    // Если да, обновляем его статус в базе данных на "Онлайн"
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null && user.name!="") {
            AppStates.updateState(currentUser.uid, AppStates.ONLINE)
        }
    }

    // При остановке активити проверяем, аутентифицирован ли текущий пользователь
    // Если да, обновляем его статус в базе данных на "Оффлайн"
    override fun onStop() {
        super.onStop()
        val currentUser = auth.currentUser
        if (currentUser != null && user.name!="") {
            AppStates.updateState(currentUser.uid, AppStates.OFFLINE)
        }
    }
}
