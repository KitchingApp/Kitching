package com.kitchingapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.R
import com.kitchingapp.data.database.dto.OrderCategoryDTO
import com.kitchingapp.databinding.ItemBigCategoryBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class OrderAdapter(private val lifecycleOwner: LifecycleOwner, private val navController: NavController): ListAdapter<OrderCategoryDTO, OrderAdapter.OrderViewHolder>(diffUtil) {
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
        val diffUtil = object : DiffUtil.ItemCallback<OrderCategoryDTO>() {
            override fun areItemsTheSame(
                oldItem: OrderCategoryDTO,
                newItem: OrderCategoryDTO
            ): Boolean {
                return oldItem.categoryName == newItem.categoryName
            }

            override fun areContentsTheSame(
                oldItem: OrderCategoryDTO,
                newItem: OrderCategoryDTO
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class OrderViewHolder(val binding: ItemBigCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindBigCategory(orderCategory: OrderCategoryDTO) {
            with(binding) {
                categoryNameTV.text = orderCategory.categoryName
                categoryCV.setCardBackgroundColor(Color.parseColor(orderCategory.color))
                categoryCV.clicks().onEach {
                    navController.navigate(R.id.action_orderFragment_to_orderListFragment)
                }.launchIn(lifecycleOwner.lifecycleScope)
            }
        }
    }
}