package com.example.firebasetaskappkaushal.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.firebasetaskappkaushal.databinding.UserItemBinding
import com.example.firebasetaskappkaushal.model.UserInfo
import com.example.firebasetaskappkaushal.ui.adapter.viewholder.UserViewHolder

class UserInfoAdapter(private val listener: ItemClickListener) :
    androidx.recyclerview.widget.ListAdapter<UserInfo, UserViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<UserInfo>() {
        override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = currentList[position]
        holder.bind(currentItem)
    }
}

interface ItemClickListener {
    fun onItemClick(userInfo: UserInfo)
}
