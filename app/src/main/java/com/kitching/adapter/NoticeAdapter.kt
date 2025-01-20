package com.kitching.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.common.util.throttleClicks
import com.kitching.data.dto.NoticeDTO
import com.kitching.data.dto.ParcelableNoticeDTO
import com.kitching.databinding.ItemNoticeBinding
import com.kitching.view.fragment.other.notice.NoticeFragmentDirections

class NoticeAdapter(private val lifecycleOwner: LifecycleOwner) :
    ListAdapter<NoticeDTO, NoticeAdapter.NoticeViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoticeViewHolder {
        val binding = ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeViewHolder(binding, findNavController(parent))
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

    inner class NoticeViewHolder(val binding: ItemNoticeBinding, val navController: NavController) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindNotice(notice: NoticeDTO) {
            with(binding) {
                noticeDateTV.text = notice.date.toString()
                writerTV.text = notice.writerName
                noticeTitleTV.text = notice.title
                noticeContentPreviewTV.text = notice.content
                noticeCV.throttleClicks(lifecycleOwner) {
                    val parcelableNotice = ParcelableNoticeDTO(
                        dateString = notice.date.toString(),
                        noticeId = notice.noticeId,
                        writerId = notice.writerId,
                        writerName = notice.writerName,
                        title = notice.title,
                        content = notice.content
                    )
                    val action =
                        NoticeFragmentDirections.actionNoticeFragmentToNoticeDetailFragment(
                            parcelableNotice
                        )
                    navController.navigate(action)
                }
            }
        }
    }
}