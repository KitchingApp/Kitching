package com.kitchingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.databinding.ItemSmallCategoryBinding
import com.kitchingapp.domain.entities.Order
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class OrderListAdapter(private val lifecycleOwner: LifecycleOwner): ListAdapter<Order, OrderListAdapter.OrderViewHolder>(diffUtil) {
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
        val diffUtil = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(
                oldItem: Order,
                newItem: Order
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: Order,
                newItem: Order
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class OrderViewHolder(val binding: ItemSmallCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindSmallCategory(order: Order) {
            with(binding) {
                categoryNameTV.text = order.name
                categoryCV.clicks().onEach {

                }.launchIn(lifecycleOwner.lifecycleScope)
            }
        }
    }
}