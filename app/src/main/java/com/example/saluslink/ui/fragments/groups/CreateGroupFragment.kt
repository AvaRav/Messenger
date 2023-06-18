package com.example.saluslink.ui.fragments.groups

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.saluslink.R
import com.example.saluslink.models.CommonModel
import com.example.saluslink.ui.fragments.BaseFragment
import com.example.saluslink.utilits.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class CreateGroupFragment: BaseFragment(R.layout.fragment_create_group) {
    private lateinit var createGroupButton: Button
    private lateinit var titleGroup: EditText
    private lateinit var imageGroup: ImageView
    private lateinit var directionGroup: EditText
    private lateinit var themesGroup: EditText
    private lateinit var aboutTheTopicGroup: EditText
    private var gUri = Uri.EMPTY

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.create_group)
        hideKeyboard()
        createGroupButton = requireView().findViewById(R.id.create_group)
        titleGroup = requireView().findViewById(R.id.title_group)
        imageGroup = requireView().findViewById(R.id.create_group_image)
        directionGroup = requireView().findViewById(R.id.text_direction)
        themesGroup = requireView().findViewById(R.id.text_themes)
        aboutTheTopicGroup = requireView().findViewById(R.id.text_aboutTheTopic)
        imageGroup.setOnClickListener() { addImage() }
        createGroupButton.setOnClickListener {
            val titleGroup = titleGroup.text.toString()
            val directionGroup = directionGroup.text.toString()
            val themesGroup = themesGroup.text.toString()
            val aboutTheTopicGroup = aboutTheTopicGroup.text.toString()
            if (titleGroup.isEmpty() || directionGroup.isEmpty() || themesGroup.isEmpty()) {
                showToast("Введите данные для создания группы. Обязательные: название, направление, тема!")
            } else {
                createGroupDatabase(gUri, titleGroup, directionGroup, themesGroup, aboutTheTopicGroup) {
                    replaceFragment(GroupsFragment(), false)
                }
            }
        }
        titleGroup.requestFocus()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null){
            gUri = CropImage.getActivityResult(data).uri
            imageGroup.setImageURI(gUri)
        }
    }

    private fun addImage() {
        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(200, 200)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }
}