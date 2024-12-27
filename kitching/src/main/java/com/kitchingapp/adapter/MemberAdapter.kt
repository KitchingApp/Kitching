package com.kitchingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.common.throttleFirst
import com.kitchingapp.data.database.dto.MemberDTO
import com.kitchingapp.databinding.ItemMemberlistBinding
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class MemberAdapter : ListAdapter<MemberDTO, MemberAdapter.MemberViewHolder>(diffUtil){
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
        val diffUtil = object : DiffUtil.ItemCallback<MemberDTO>() {
            override fun areItemsTheSame(
                oldItem: MemberDTO,
                newItem: MemberDTO
            ): Boolean {
                return oldItem.userName == newItem.userName
            }

            override fun areContentsTheSame(
                oldItem: MemberDTO,
                newItem: MemberDTO
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class MemberViewHolder(val binding: ItemMemberlistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindMember(member: MemberDTO) {
            with(binding) {
                nameTV.text = member.userName
                departmentTV.text = member.departmentName
                staffLevelTV.text = member.staffLevelName
                memberListCV.clicks().throttleFirst().onEach {

                }
            }

        }
    }
}