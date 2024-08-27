package com.example.saluslink.ui.fragments.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.saluslink.R
import com.example.saluslink.activities.RegisterActivity
import com.example.saluslink.ui.fragments.BaseFragment
import com.example.saluslink.utilits.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)

        // Инициализация элементов интерфейса
        val name = requireView().findViewById<TextView>(R.id.settings_name)
        val change_name = requireView().findViewById<TextView>(R.id.settings_fio)
        val status = requireView().findViewById<TextView>(R.id.settings_text_online_offline)
        val photo = requireView().findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.settings_user_photo)
        val changeNameButton = view?.findViewById<ConstraintLayout>(R.id.settings_btn_change_fio)
        val changeAboutButton = view?.findViewById<ConstraintLayout>(R.id.settings_btn_change_about)
        val ChangePhotoButton = requireView().findViewById<ConstraintLayout>(R.id.settings_btn_change_photo)

        // Установка текста и изображения на основе данных пользователя
        name.text = user.name
        change_name.text = user.name + " " + user.surname
        status.text = user.status
        photo.downloadAndSetImage(user.photoUrl)

        // Обработчик нажатия на кнопку изменения фотографии пользователя
        ChangePhotoButton?.setOnClickListener{
            changeUserPhoto()
        }

        // Обработчик нажатия на кнопку изменения имени и фамилии пользователя
        changeNameButton?.setOnClickListener{
            replaceFragment(ChangeNameFragment(), true)
        }

        // Обработчик нажатия на кнопку изменения о себе
        changeAboutButton?.setOnClickListener{
            replaceFragment(ChangeAboutFragment(), true)
        }
    }

    private fun changeUserPhoto() {
        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(200, 200)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Создание меню в верхнем правом углу
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                // Обработчик нажатия на пункт меню "Выход"
                AppStates.updateState(uid, AppStates.OFFLINE)

                auth.signOut()
                (APP_ACTIVITY).replaceActivity(RegisterActivity())
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            // Получение обрезанного изображения
            val uri = CropImage.getActivityResult(data).uri
            val path = ref_storage_root.child(folder_profile_image).child(uid)
            val photo = requireView().findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.settings_user_photo)

            // Загрузка изображения в хранилище Firebase Storage
            putFileToStorage(uri, path){
                // Получение URL-адреса загруженного изображения
                getUrlFromStorage(path){
                    // Обновление URL-адреса изображения в базе данных
                    putUrlToDatabase(it){
                        photo.downloadAndSetImage(it)
                        user.photoUrl = it
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                    }
                }
            }
        }
    }
}
