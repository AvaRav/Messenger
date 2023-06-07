package com.example.saluslink.ui.objects

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.example.saluslink.R
import com.example.saluslink.ui.fragments.UsersFragment
import com.example.saluslink.ui.fragments.MessageFragment
import com.example.saluslink.ui.fragments.ProfileFragment
import com.example.saluslink.ui.fragments.settings.SettingsFragment
import com.example.saluslink.utilits.APP_ACTIVITY
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

class AppDrawer () {
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
            .withActivity(APP_ACTIVITY)
            .withToolbar(APP_ACTIVITY.mToolbar)
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
                    .withName("Пользователи")
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
                    clickToItem(position)
                    return false
                }
            }).build()
    }

    private fun clickToItem(position: Int){
        when(position){
            1 -> APP_ACTIVITY.replaceFragment(ProfileFragment())
            3 -> APP_ACTIVITY.replaceFragment(MessageFragment())
            7 -> APP_ACTIVITY.replaceFragment(UsersFragment())
            11 -> APP_ACTIVITY.replaceFragment(SettingsFragment())
        }
    }

    private fun createHeader() {
        mCurrentProfile = ProfileDrawerItem()
            .withName(user.name+ " " + user.surname)
            .withIcon(user.photoUrl)
            .withIdentifier(200)
        mHeader = AccountHeaderBuilder()
            .withActivity(APP_ACTIVITY)
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