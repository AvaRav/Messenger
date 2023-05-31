package com.example.saluslink.utilits

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AppValueEventListener(val onSuccess:(DataSnapshot) -> Unit) :ValueEventListener {
    override fun onDataChange(p0: DataSnapshot) {
        onSuccess(p0)
    }

    override fun onCancelled(p0: DatabaseError) {
    }
}