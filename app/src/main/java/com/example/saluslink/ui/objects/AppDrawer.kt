package com.example.saluslink.ui.objects

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.saluslink.R
import com.example.saluslink.ui.fragments.MessageFragment
import com.example.saluslink.ui.fragments.ProfileFragment
import com.example.saluslink.ui.fragments.SettingsFragment
import com.example.saluslink.utilits.downloadAndSetImage
import com.example.saluslink.utilits.replaceFragment
import com.example.saluslink.utilits.user
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader

class AppDrawer (val mainActivity: AppCompatActivity,val toolbar: Toolbar) {
    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
    private lateinit var mCurrentProfile: ProfileDrawerItem

    fun create(){
        initLoader()
        createHeader()
        createDrawer()
    }

    private fun createDrawer() {
        mDrawer = DrawerBuilder()
            .withActivity(mainActivity)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(true)
            .withSliderBackgroundDrawableRes(R.drawable.menu_background)
            .withSelectedItem(-1)
            .withAccountHeader(mHeader)
            .addDrawerItems(
                PrimaryDrawerItem()
                    .withIdentifier(100)
                    .withName("Профиль")
                    .withSelectable(false),
                DividerDrawerItem(),
                PrimaryDrawerItem()
                    .withIdentifier(101)
                    .withName("Сообщения")
                    .withSelectable(false),
                DividerDrawerItem(),
                PrimaryDrawerItem()
                    .withIdentifier(102)
                    .withName("Группы")
                    .withSelectable(false),
                DividerDrawerItem(),
                PrimaryDrawerItem()
                    .withIdentifier(103)
                    .withName("Друзья")
                    .withSelectable(false),
                DividerDrawerItem(),
                PrimaryDrawerItem()
                    .withIdentifier(104)
                    .withName("Календарь")
                    .withSelectable(false),
                DividerDrawerItem(),
                PrimaryDrawerItem()
                    .withIdentifier(105)
                    .withName("Настройки")
                    .withSelectable(false),
                DividerDrawerItem()
            ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener{
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    when(position){
                        1 -> mainActivity.replaceFragment(ProfileFragment())
                        3 -> mainActivity.replaceFragment(MessageFragment())
                        11 -> mainActivity.replaceFragment(SettingsFragment())
                    }
                    return false
                }
            }).build()
    }

    private fun createHeader() {
        mCurrentProfile = ProfileDrawerItem()
            .withName(user.name+ " " + user.surname)
            .withIcon(user.photoUrl)
            .withIdentifier(200)
        mHeader = AccountHeaderBuilder()
            .withActivity(mainActivity)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                mCurrentProfile
            ).build()
    }

    fun updateHeader(){
        mCurrentProfile
            .withName(user.name + " " + user.surname)
            .withIcon(user.photoUrl)
        mHeader.updateProfile(mCurrentProfile)
    }

    private fun initLoader(){
        DrawerImageLoader.init(object :AbstractDrawerImageLoader(){
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable) {
                imageView.downloadAndSetImage(uri.toString())
            }
        })
    }
}