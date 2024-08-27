package com.example.saluslink.ui.fragments.groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.models.CommonModel
import com.example.saluslink.ui.fragments.single_chat.GroupsChatFragment
import com.example.saluslink.utilits.APP_ACTIVITY
import com.example.saluslink.utilits.downloadAndSetImage
import com.example.saluslink.utilits.replaceFragment
import de.hdodenhof.circleimageview.CircleImageView

class GroupsAdapter : RecyclerView.Adapter<GroupsAdapter.GroupsListHolder>() {

    // Список групп
    private val listGroups = mutableListOf<CommonModel>()

    // ViewHolder для элемента списка групп
    class GroupsListHolder(view: View) : RecyclerView.ViewHolder(view) {
        val groupName: TextView = view.findViewById(R.id.title_group)
        val groupDirection: TextView = view.findViewById(R.id.direction)
        val groupPhoto: CircleImageView = view.findViewById(R.id.group_photo)
    }

    // Создание ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsListHolder {
        // Создание View для элемента списка групп
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group, parent, false)
        val holder = GroupsListHolder(view)

        // Обработчик нажатия на элемент списка
        holder.itemView.setOnClickListener {
            // Замена текущего фрагмента на фрагмент чата группы
            APP_ACTIVITY.replaceFragment(GroupsChatFragment(listGroups[holder.adapterPosition]))
        }

        return holder
    }

    // Получение количества элементов списка
    override fun getItemCount(): Int = listGroups.size

    // Привязка данных к ViewHolder
    override fun onBindViewHolder(holder: GroupsListHolder, position: Int) {
        holder.groupName.text = listGroups[position].title
        holder.groupDirection.text = listGroups[position].direction
        holder.groupPhoto.downloadAndSetImage(listGroups[position].photoUrl)
    }

    // Обновление списка групп
    fun updateListGroup(item: CommonModel) {
        listGroups.add(item)
        notifyItemInserted(listGroups.size)
    }
}
