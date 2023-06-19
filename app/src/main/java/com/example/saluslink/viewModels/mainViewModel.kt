package com.example.saluslink.utilits

import android.net.Uri
import android.util.Log
import com.example.saluslink.models.CommonModel
import com.example.saluslink.models.Group
import com.example.saluslink.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

lateinit var auth: FirebaseAuth
lateinit var ref_database_root: DatabaseReference
lateinit var uid: String
lateinit var user: User
lateinit var group: Group
lateinit var ref_storage_root: StorageReference

const val folder_profile_image = "profile_image"
const val folder_files = "messages_files"
const val group_image = "groups_image"

const val node_groups = "groups"
const val node_messages = "messages"


fun initFirebase(){
    auth = FirebaseAuth.getInstance()
    ref_database_root = FirebaseDatabase.getInstance().reference
    user = User()
    group = Group()
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

inline fun putFileToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
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
    mapMessage["id"] = messageKey.toString()
    mapMessage["timeStamp"] = ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    ref_database_root
        .updateChildren(mapDialog)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast("Не удалось отправить сообщение") }
}

fun sendGroupMessage(message: String, groupId: String, typeText: String, function: () -> Unit) {
    val refMesGroup = "$node_groups/$groupId/$node_messages"
    val messageKey = ref_database_root.child(refMesGroup).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage["from"] = uid
    mapMessage["type"] = typeText
    mapMessage["text"] = message
    mapMessage["id"] = messageKey.toString()
    mapMessage["timeStamp"] = ServerValue.TIMESTAMP

    ref_database_root.child(refMesGroup).child(messageKey.toString())
        .updateChildren(mapMessage)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast("Не удалось отправить сообщение") }
}

fun sendMessageAsFile(receivingUserID: String, fileUrl: String, messageKey: String, typeMessage: String, filename: String) {
    val refDialogUser = "/messages/$uid/$receivingUserID"
    val refDialogReceivingUser = "/messages/$receivingUserID/$uid"

    val mapMessage = hashMapOf<String, Any>()
    mapMessage["from"] = uid
    mapMessage["type"] = typeMessage
    mapMessage["id"] = messageKey
    mapMessage["timeStamp"] = ServerValue.TIMESTAMP
    mapMessage["fileUrl"] = fileUrl
    mapMessage["text"] = filename

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    ref_database_root
        .updateChildren(mapDialog)
        .addOnFailureListener { showToast("Не удалось отправить сообщение") }
}

fun sendGroupMessageAsFile(groupId: String, fileUrl: String, messageKey: String, typeMessage: String, filename: String) {
    val refMesGroup = "$node_groups/$groupId/$node_messages"
    val messageKeyGroup = ref_database_root.child(refMesGroup).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage["from"] = uid
    mapMessage["type"] = typeMessage
    mapMessage["id"] = messageKey
    mapMessage["timeStamp"] = ServerValue.TIMESTAMP
    mapMessage["fileUrl"] = fileUrl
    mapMessage["text"] = filename

    ref_database_root.child(refMesGroup).child(messageKeyGroup.toString())
        .updateChildren(mapMessage)
        .addOnFailureListener { showToast("Не удалось отправить сообщение") }
}

fun getMessageKey(id: String) =
    ref_database_root.child("messages").child(uid).child(id).push().key.toString()

fun uploadFileToStorage(uri: Uri, messageKey:String, id: String, typeMessage:String, filename: String = ""){
    val path = ref_storage_root.child(folder_files).child(messageKey)

    putFileToStorage(uri, path){
        getUrlFromStorage(path){
            sendMessageAsFile(id, it, messageKey, typeMessage, filename)
        }
    }
}

fun uploadFileToStorageToGroup(uri: Uri, messageKey: String, groupId: String, typeMessage: String, filename: String = "") {
    val path = ref_storage_root.child("$node_groups/$groupId/$node_messages").child(messageKey)

    putFileToStorage(uri, path) {
        getUrlFromStorage(path) { fileUrl ->
            sendGroupMessageAsFile(groupId, fileUrl, messageKey, typeMessage, filename)
        }
    }
}

fun getFileFromStorage(mFile: File, fileUrl: String, function: () -> Unit) {
    val path = ref_storage_root.storage.getReferenceFromUrl(fileUrl)
    path.getFile(mFile)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast("Не удалось получить файл") }
}

fun saveToMessageList(id: String, type: String) {
    val refUser = "message_list/$uid/$id"
    val refReceived = "message_list/$id/$uid"

    val mapUser = hashMapOf<String, Any>()
    val mapReceived = hashMapOf<String, Any>()

    mapUser["id"] = id
    mapUser["type"] = type

    mapReceived["id"] = uid
    mapReceived["type"] = type

    val commonMap = hashMapOf<String, Any>()
    commonMap[refUser] = mapUser
    commonMap[refReceived] = mapReceived

    ref_database_root.updateChildren(commonMap)
        .addOnFailureListener { showToast("Ошибка загрузки чатов") }
}

fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): User =
    this.getValue(User::class.java) ?: User()

fun DataSnapshot.getGroupModel(): Group =
    this.getValue(Group::class.java) ?: Group()

fun deleteChat(id: String, function: () -> Unit) {
    ref_database_root.child("message_list").child(uid).child(id).removeValue()
        .addOnFailureListener { showToast("Не удалось удалить чат") }
        .addOnSuccessListener { function() }
}

fun deleteGroup(groupId: String, function: () -> Unit) {
    ref_database_root.child(node_groups).child(groupId).removeValue()
        .addOnFailureListener { showToast("Не удалось удалить группу") }
        .addOnSuccessListener { function() }
}

fun clearChat(id: String, function: () -> Unit) {
    ref_database_root.child("messages").child(uid).child(id)
        .removeValue()
        .addOnFailureListener { showToast("Не удалось отчистить чат") }
        .addOnSuccessListener { ref_database_root.child("messages").child(id).child(uid)
            .removeValue()
            .addOnSuccessListener { function() }}
        .addOnFailureListener { showToast("Не удалось отчистить чат") }
}

fun createGroupDatabase(uri: Uri, titleGroup: String, directionGroup: String, themesGroup: String, aboutTheTopicGroup: String, function: () -> Unit) {
    val keyGroup = ref_database_root.child("groups").push().key.toString()
    val path = ref_database_root.child("groups").child(keyGroup)
    val pathStor = ref_storage_root.child(group_image).child(keyGroup)

    val groupData = hashMapOf<String, Any>()
    groupData["id"] = keyGroup
    groupData["title"] = titleGroup
    groupData["direction"] = directionGroup
    groupData["themes"] = themesGroup
    groupData["aboutTheTopic"] = aboutTheTopicGroup
    groupData["photoUrl"] = "empty"

    path.updateChildren(groupData)
        .addOnSuccessListener {
            if (uri != Uri.EMPTY) {
                putFileToStorage(uri, pathStor) {
                    getUrlFromStorage(pathStor) {
                        path.child("photoUrl").setValue(it)
                        addGroupToMessageList(keyGroup) {
                            function()
                        }
                    }
                }
            } else {
                addGroupToMessageList(keyGroup) {
                    function()
                }
            }
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun addGroupToMessageList(groupId: String, function: (Any?) -> Unit) {
    val path = ref_database_root.child("message_list")
    val groupData = hashMapOf<String, Any>()
    groupData["id"] = groupId
    groupData["type"] = "groups"

    ref_database_root.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            dataSnapshot.children.forEach { userSnapshot ->
                val userId = userSnapshot.key.toString()
                path.child(userId).child(groupId).setValue(groupData)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e("Firebase", "Ошибка при чтении базы данных: ${databaseError.message}")
        }
    })

    path.child(uid).child(groupId).setValue(groupData)
        .addOnSuccessListener {
            function(null)
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}


