package com.example.saluslink.viewModels

import androidx.lifecycle.ViewModel
import com.example.saluslink.models.Group
import com.example.saluslink.utilits.APP_ACTIVITY
import com.example.saluslink.utilits.replaceFragment
import com.example.saluslink.ui.fragments.GroupProfileFragment
import com.google.firebase.database.FirebaseDatabase

class GroupInfoViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val groupRef = database.reference.child("groups")

    fun changeGroupInfo(groupId: String, title: String, direction: String, themes: String, aboutTheTopic: String) {
        if (title.isNotEmpty()) {
            groupRef.child(groupId).child("title").setValue(title)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        updateGroupData(groupId, title, direction, themes, aboutTheTopic)
                    }
                }
        }

        if (direction.isNotEmpty()) {
            groupRef.child(groupId).child("direction").setValue(direction)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        updateGroupData(groupId, title, direction, themes, aboutTheTopic)
                    }
                }
        }

        if (themes.isNotEmpty()) {
            groupRef.child(groupId).child("themes").setValue(themes)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        updateGroupData(groupId, title, direction, themes, aboutTheTopic)
                    }
                }
        }

        if (aboutTheTopic.isNotEmpty()) {
            groupRef.child(groupId).child("aboutTheTopic").setValue(aboutTheTopic)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        updateGroupData(groupId, title, direction, themes, aboutTheTopic)
                    }
                }
        }
    }

    private fun updateGroupData(groupId: String, title: String, direction: String, themes: String, aboutTheTopic: String) {
        val updatedGroup = Group(groupId, title, direction, themes, aboutTheTopic)
        APP_ACTIVITY.replaceFragment(GroupProfileFragment(updatedGroup))
    }
}