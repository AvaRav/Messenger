package com.example.saluslink.ui.fragments.groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.models.CommonModel
import com.example.saluslink.models.Group
import com.example.saluslink.ui.fragments.single_chat.GroupsChatFragment
import com.example.saluslink.ui.fragments.single_chat.SingleChatFragment
import com.example.saluslink.utilits.APP_ACTIVITY
import com.example.saluslink.utilits.downloadAndSetImage
import com.example.saluslink.utilits.downloadAndSetImageForGroup
import com.example.saluslink.utilits.replaceFragment
import de.hdodenhof.circleimageview.CircleImageView

class GroupsAdapter : RecyclerView.Adapter<GroupsAdapter.GroupsListHolder> () {

    private val listGroups = mutableListOf<CommonModel>()

    class GroupsListHolder(view: View): RecyclerView.ViewHolder(view) {
        val groupName: TextView = view.findViewById(R.id.title_group)
        val groupDirection: TextView = view.findViewById(R.id.direction)
        val groupPhoto: CircleImageView = view.findViewById(R.id.group_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group, parent, false)
        val holder = GroupsListHolder(view)
        holder.itemView.setOnClickListener {
            APP_ACTIVITY.replaceFragment(GroupsChatFragment(listGroups[holder.adapterPosition]))
        }
        return holder
    }

    override fun getItemCount(): Int = listGroups.size

    override fun onBindViewHolder(holder: GroupsListHolder, position: Int) {
        holder.groupName.text = listGroups[position].title
        holder.groupDirection.text = listGroups[position].direction
        holder.groupPhoto.downloadAndSetImageForGroup(listGroups[position].photoUrl)
    }

    fun updateListGroup(item: CommonModel) {
        listGroups.add(item)
        notifyItemInserted(listGroups.size)
    }
}