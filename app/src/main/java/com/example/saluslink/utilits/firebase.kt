package com.example.saluslink.utilits

import android.net.Uri
import com.example.saluslink.models.CommonModel
import com.example.saluslink.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var auth: FirebaseAuth
lateinit var ref_database_root: DatabaseReference
lateinit var uid: String
lateinit var user: User
lateinit var ref_storage_root: StorageReference

const val folder_profile_image = "profile_image"

fun initFirebase(){
    auth = FirebaseAuth.getInstance()
    ref_database_root = FirebaseDatabase.getInstance().reference
    user = User()
    uid = auth.currentUser?.uid.toString()
    ref_storage_root = FirebaseStorage.getInstance().reference
}

inline fun putUrlToDatabase(url: String,crossinline function: () -> Unit) {
    ref_database_root.child("users").child(uid).child("photoUrl").setValue(url)
        .addOnSuccessListener {function()}
        .addOnFailureListener { showToast("Ошибка!") }
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url:String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener {function(it.toString())}
        .addOnFailureListener { showToast("Ошибка!") }
}

inline fun putImageStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener {function()}
        .addOnFailureListener { showToast("Ошибка!") }
}

inline fun initUser(crossinline function: () -> Unit) {
    ref_database_root.child("users").child(uid)
        .addListenerForSingleValueEvent(AppValueEventListener {
            user = it.getValue(User::class.java) ?: User()
            function()
        })
}

fun sendMessage(message: String, receivingUserID: String, typeText: String, function: () -> Unit) {
    val refDialogUser = "/messages/$uid/$receivingUserID"
    val refDialogReceivingUser = "/messages/$receivingUserID/$uid"
    val messageKey = ref_database_root.child(refDialogUser).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage["from"] = uid
    mapMessage["type"] = typeText
    mapMessage["text"] = message
    mapMessage["timeStamp"] = ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    ref_database_root
        .updateChildren(mapDialog)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast("Не удалось отправить сообщение") }
}


fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): User =
    this.getValue(User::class.java) ?: User()