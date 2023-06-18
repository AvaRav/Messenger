package com.example.saluslink.ui.fragments.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.saluslink.R
import com.example.saluslink.databinding.FragmentGroupsBinding
import com.example.saluslink.models.CommonModel
import com.example.saluslink.models.Group
import com.example.saluslink.ui.fragments.BaseFragment
import com.example.saluslink.utilits.*

class GroupsFragment: BaseFragment(R.layout.fragment_groups) {
    private lateinit var createGroup: Button
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: GroupsAdapter

    private val mRefGroups = ref_database_root.child("groups")
    private var mListGroups = listOf<CommonModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        createGroup = view.findViewById(R.id.create_group_button)
        createGroup.setOnClickListener {
            replaceFragment(CreateGroupFragment(), true)
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = requireView().findViewById(R.id.list_group)
        mAdapter = GroupsAdapter()
        mRefGroups.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
            mListGroups = dataSnapshot.children.map { it.getCommonModel() }
            mListGroups.forEach { groupModel ->
                mAdapter.updateListGroup(groupModel)
            }
            mRecyclerView.adapter = mAdapter
        })
    }
}



