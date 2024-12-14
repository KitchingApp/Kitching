package com.kitchingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.databinding.ItemMemberlistBinding
import com.kitchingapp.domain.entities.User

class MemberAdapter : ListAdapter<User, MemberAdapter.MemberViewHolder>(diffUtil){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemberViewHolder {
        val binding = ItemMemberlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MemberViewHolder,
        position: Int
    ) {
        holder.bindMember(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class MemberViewHolder(val binding: ItemMemberlistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindMember(user: User) {
            with(binding) {
                nameTV.text = user.name
            }

        }
    }
}