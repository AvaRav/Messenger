package com.example.saluslink.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.databinding.FragmentFriendsBinding
import com.example.saluslink.databinding.FragmentMessageBinding
import com.example.saluslink.models.CommonModel
import com.example.saluslink.utilits.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView

class FriendsFragment : BaseFragment(R.layout.fragment_friends) {
    private lateinit var mBinding: FragmentFriendsBinding

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, UsersHolder>
    private lateinit var mRefFriends:DatabaseReference

    override fun onResume() {
        super.onResume()
        initRecycleView()
    }

    private fun initRecycleView() {
        mRecyclerView = requireView().findViewById(R.id.friend_recycle_view)
        mRefFriends = ref_database_root.child("users").child(uid)

        val query = ref_database_root.child("users")
            .orderByChild("status")
            .equalTo("в сети")
            .limitToFirst(30)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(query, CommonModel::class.java)
            .build()

        mAdapter = object : FirebaseRecyclerAdapter<CommonModel, UsersHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_item, parent, false)
                return UsersHolder(view)
            }

            @SuppressLint("SetTextI18n")
            override fun onBindViewHolder(holder: UsersHolder, position: Int, model: CommonModel) {
                holder.name.text = model.name + " " + model.surname
                holder.status.text = model.status
                holder.photo.downloadAndSetImage(model.photoUrl)
            }
        }

        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }

    class UsersHolder(view: View):RecyclerView.ViewHolder(view){
        val name:TextView = view.findViewById(R.id.friend_name)
        val status:TextView = view.findViewById(R.id.status)
        val photo:CircleImageView = view.findViewById(R.id.friend_photo)
    }

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
    }
}