package com.example.saluslink.utilits

import com.example.saluslink.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

lateinit var auth: FirebaseAuth
lateinit var ref_database_root: DatabaseReference
lateinit var uid: String
lateinit var user: User

fun initFirebase(){
    auth = FirebaseAuth.getInstance()
    ref_database_root = FirebaseDatabase.getInstance().reference
    user = User()
    uid = auth.currentUser?.uid.toString()
}