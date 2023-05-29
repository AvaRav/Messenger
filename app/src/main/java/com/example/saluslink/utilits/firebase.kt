package com.example.saluslink.utilits

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

lateinit var auth: FirebaseAuth
lateinit var ref_database_root: DatabaseReference

const val NODE_USERS = "users"
const val CHILD_ID = "id"
const val CHILD_NAME = "name"
const val CHILD_SURNAME = "surname"
const val CHILD_WORKPLACE = "workplace"
const val CHILD_SPECIALIZATION = "specialization"

fun initFirebase(){
    auth = FirebaseAuth.getInstance()
    ref_database_root = FirebaseDatabase.getInstance().reference
}