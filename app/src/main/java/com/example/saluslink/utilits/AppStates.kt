package com.example.saluslink.utilits

enum class AppStates(val state:String) {
    ONLINE("в сети"),
    OFFLINE("был недавно"),
    TYPING("печатает...");

    companion object{
        fun updateState(appStates: AppStates){
            ref_database_root.child("users").child(uid).child("status").setValue(appStates.state)
                .addOnSuccessListener { user.status = appStates.state }
        }
    }
}