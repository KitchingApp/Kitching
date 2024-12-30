package com.kitching.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.common.throttleFirst
import com.kitching.data.database.dto.OrderDTO
import com.kitching.databinding.ItemSmallCategoryBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class OrderListAdapter(private val lifecycleOwner: LifecycleOwner): ListAdapter<OrderDTO, OrderListAdapter.OrderViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderViewHolder {
        val binding = ItemSmallCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OrderViewHolder,
        position: Int
    ) {
        holder.bindSmallCategory(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<OrderDTO>() {
            override fun areItemsTheSame(
                oldItem: OrderDTO,
                newItem: OrderDTO
            ): Boolean {
                return oldItem.orderName == newItem.orderName
            }

            override fun areContentsTheSame(
                oldItem: OrderDTO,
                newItem: OrderDTO
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class OrderViewHolder(val binding: ItemSmallCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindSmallCategory(order: OrderDTO) {
            with(binding) {
                categoryNameTV.text = order.orderName
                categoryCV.clicks().throttleFirst().onEach {

                }.launchIn(lifecycleOwner.lifecycleScope)
            }
        }
    }
}