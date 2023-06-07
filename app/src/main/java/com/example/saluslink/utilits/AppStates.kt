package com.example.saluslink.utilits

enum class AppStates(val state:String) {
    ONLINE("в сети"),
    OFFLINE("не в сети"),
    TYPING("печатает...");

    companion object {
        fun updateState(uid: String, appStates: AppStates) {
            val userRef = ref_database_root.child("users").child(uid)
            userRef.child("status").setValue(appStates.state)
                .addOnSuccessListener {
                    userRef.child("status").setValue(appStates.state)
                }
        }
    }
}

