package com.example.saluslink.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.saluslink.models.Group
import com.example.saluslink.utilits.createGroupDatabase

class CreateGroupViewModel : ViewModel() {

    private val _groupCreated = MutableLiveData<Boolean>()
    val groupCreated: LiveData<Boolean> get() = _groupCreated

    fun createGroup(group: Group, imageUri: Uri) {
        createGroupDatabase(imageUri, group.title, group.direction, group.themes, group.aboutTheTopic) {
            _groupCreated.value = true
        }
    }
}
