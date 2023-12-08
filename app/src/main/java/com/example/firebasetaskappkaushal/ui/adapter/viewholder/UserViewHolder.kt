package com.example.firebasetaskappkaushal.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasetaskappkaushal.R
import com.example.firebasetaskappkaushal.databinding.UserItemBinding
import com.example.firebasetaskappkaushal.model.UserInfo
import com.example.firebasetaskappkaushal.ui.adapter.ItemClickListener

class UserViewHolder(
    private val binding: UserItemBinding,
    private val listener: ItemClickListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(userInfo: UserInfo) {
        binding.apply {
            val context = root.context
            tvFirstName.text = context.getString(R.string.first_name, userInfo.firstName)
            tvLastName.text = context.getString(R.string.last_name, userInfo.lastName)
            tvAge.text = context.getString(R.string.age, userInfo.age)
            tvGender.text = context.getString(R.string.gender, userInfo.gender)
            tvClassroom.text = context.getString(R.string.classroom, userInfo.classRoom)
            Glide.with(binding.ivUser)
                .load(userInfo.image)
                .placeholder(R.drawable.ic_user)
                .into(binding.ivUser)
        }
        binding.root.setOnClickListener {
            listener.onItemClick(userInfo)
        }
    }
}
