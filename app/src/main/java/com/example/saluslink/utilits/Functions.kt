package com.example.saluslink.utilits

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.saluslink.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

fun showToast(message: String) {
    val toast = Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.TOP, 0, 0)
    toast.show()
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, addStack: Boolean = true) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(R.id.data_container, fragment)
    if (addStack) {
        transaction.addToBackStack(null)
    }
    transaction.commitAllowingStateLoss()
}

fun Fragment.replaceFragment(fragment: Fragment, b: Boolean) {
    this.fragmentManager?.beginTransaction()
        ?.addToBackStack(null)?.replace(R.id.data_container, fragment)?.commit()
}

fun hideKeyboard(){
    val imm: InputMethodManager = APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken, 0)
}

fun ImageView.downloadAndSetImage(url:String?){
    if (!url.isNullOrEmpty()) {
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.camera)
            .into(this)
    } else {
        setImageResource(R.drawable.camera)
    }
}

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}

@SuppressLint("Range")
fun getFilenameFromUri(uri: Uri): String {
    var result = ""
    val cursor = APP_ACTIVITY.contentResolver.query(uri, null, null, null, null)
    try {
        if (cursor!= null && cursor.moveToFirst()){
            result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    } catch (e:Exception){
        showToast("Не удалось прочитать имя файла")
    } finally {
        cursor?.close()
        return result
    }
}