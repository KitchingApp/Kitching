package com.kitchingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.R
import com.kitchingapp.databinding.ItemBigCategoryBinding
import com.kitchingapp.domain.entities.OrderCategory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class OrderAdapter(private val lifecycleOwner: LifecycleOwner, private val navController: NavController): ListAdapter<OrderCategory, OrderAdapter.OrderViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderViewHolder {
        val binding = ItemBigCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OrderViewHolder,
        position: Int
    ) {
        holder.bindBigCategory(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<OrderCategory>() {
            override fun areItemsTheSame(
                oldItem: OrderCategory,
                newItem: OrderCategory
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: OrderCategory,
                newItem: OrderCategory
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class OrderViewHolder(val binding: ItemBigCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindBigCategory(orderCategory: OrderCategory) {
            with(binding) {
                categoryNameTV.text = orderCategory.name
                categoryCV.setCardBackgroundColor(orderCategory.color)
                categoryCV.clicks().onEach {
                    navController.navigate(R.id.action_orderFragment_to_orderListFragment)
                }.launchIn(lifecycleOwner.lifecycleScope)
            }
        }
    }
}