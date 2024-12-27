package com.kitchingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.data.database.dto.NoticeDTO
import com.kitchingapp.databinding.ItemNoticeBinding

class NoticeAdapter : ListAdapter<NoticeDTO, NoticeAdapter.NoticeViewHolder>(diffUtil){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoticeViewHolder {
        val binding = ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NoticeViewHolder,
        position: Int
    ) {
        holder.bindNotice(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<NoticeDTO>() {
            override fun areItemsTheSame(
                oldItem: NoticeDTO,
                newItem: NoticeDTO
            ): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: NoticeDTO,
                newItem: NoticeDTO
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class NoticeViewHolder(val binding: ItemNoticeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindNotice(notice: NoticeDTO) {
            with(binding) {
                dateTV.text = notice.date.toString()
                writerTV.text = notice.writerName
                noticeTitleTV.text = notice.title
                noticeContentPreviewTV.text = notice.content
            }
        }
    }
}