package com.example.saluslink.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import com.example.saluslink.R
import com.example.saluslink.models.Group
import com.example.saluslink.ui.fragments.groups.GroupInfoFragment
import com.example.saluslink.utilits.APP_ACTIVITY
import com.example.saluslink.utilits.downloadAndSetImage
import com.example.saluslink.utilits.replaceFragment
import com.google.firebase.database.FirebaseDatabase
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class GroupProfileFragment(private val model: Group) : BaseFragment(R.layout.fragment_group_profile) {

    // Вызывается при возобновлении фрагмента
    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        updateProfile()
        val photoGroup = requireView().findViewById<CircleImageView>(R.id.group_profile_photo)
        photoGroup.downloadAndSetImage(model.photoUrl)

        // Обработчик нажатия на фото группы
        photoGroup.setOnClickListener {
            changePhotoGroup()
        }
    }

    // Метод для изменения фото группы
    private fun changePhotoGroup() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(200, 200)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    // Метод, вызываемый при получении результата из другого активити (например, CropImage)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val uri = CropImage.getActivityResult(data).uri
            val photoGroup = requireView().findViewById<CircleImageView>(R.id.group_profile_photo)

            updateGroupPhoto(uri)
            photoGroup.downloadAndSetImage(uri.toString())
        }
    }

    // Метод для обновления фото группы в базе данных
    private fun updateGroupPhoto(uri: Uri) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val groupRef = database.reference.child("groups").child(model.id)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val dataSnapshot = withContext(Dispatchers.IO) {
                    groupRef.get().await()
                }

                val photoUrl = dataSnapshot.child("photoUrl").getValue(String::class.java)

                requireView().findViewById<CircleImageView>(R.id.group_profile_photo)
                    .downloadAndSetImage(uri.toString())

                withContext(Dispatchers.IO) {
                    groupRef.child("photoUrl").setValue(uri.toString()).await()
                }
            } catch (e: Exception) { }
        }
    }

    // Метод для создания меню
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.group_change_menu, menu)
    }

    // Обработчик выбора пункта меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_change_group -> {
                APP_ACTIVITY.replaceFragment(GroupInfoFragment(model))
            }
        }
        return true
    }

    // Метод для обновления профиля группы
    private fun updateProfile() {
        requireView().findViewById<TextView>(R.id.name_group).text = model.title
        requireView().findViewById<TextView>(R.id.group_profile_direction).text = model.direction
        requireView().findViewById<TextView>(R.id.group_profile_themes).text = model.themes
        requireView().findViewById<TextView>(R.id.group_profile_aboutTheTopic).text = model.aboutTheTopic
    }
}
