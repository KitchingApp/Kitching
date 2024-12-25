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
import com.kitchingapp.data.database.dto.OrderCategoryDTO
import com.kitchingapp.databinding.ItemBigCategoryBinding
import com.kitchingapp.view.fragment.order.OrderFragmentDirections
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class OrderCategoryAdapter(private val lifecycleOwner: LifecycleOwner, private val navController: NavController): ListAdapter<OrderCategoryDTO, OrderCategoryAdapter.OrderViewHolder>(diffUtil) {
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
                    val argsAction = OrderFragmentDirections.actionOrderFragmentToOrderListFragment(orderCategory.categoryId)
                    navController.navigate(argsAction)
                }.launchIn(lifecycleOwner.lifecycleScope)
            }
        }
    }
}