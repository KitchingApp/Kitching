package com.kitching.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.common.util.throttleClicks
import com.kitching.data.dto.OrderCategoryDTO
import com.kitching.databinding.ItemBigCategoryBinding
import com.kitching.view.fragment.order.OrderFragmentDirections
import com.kitching.R
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.widget.itemClicks

class OrderCategoryAdapter(private val lifecycleOwner: LifecycleOwner, private val navController: NavController): ListAdapter<OrderCategoryDTO, OrderCategoryAdapter.OrderViewHolder>(diffUtil) {

    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderViewHolder {
        context = parent.context
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

                categoryCV.throttleClicks(lifecycleOwner) {
                    navigateToOrderList(orderCategory.categoryId)
                }

                optionBtn.throttleClicks(lifecycleOwner) {
                    shoPopupMenu(optionBtn, orderCategory)
                }
            }
        }
    }

    private fun navigateToOrderList(categoryId: String) {
        val action = OrderFragmentDirections.actionOrderFragmentToOrderListFragment(categoryId)
        navController.navigate(action)
    }

    private fun shoPopupMenu(anchor: View, orderCategory: OrderCategoryDTO) {
        val popup = PopupMenu(context, anchor)
        popup.menuInflater.inflate(R.menu.option_menu, popup.menu)

        popup.itemClicks().onEach {
            when(it.itemId) {
                R.id.updateInOptionMenu -> navigateToUpdateDialog(orderCategory)
                R.id.deleteInOptionMenu -> navigateToDeleteDialog(orderCategory)
            }
        }.launchIn(lifecycleOwner.lifecycleScope)

        popup.show()
    }

    private fun navigateToUpdateDialog(orderCategory: OrderCategoryDTO) {
        val action = OrderFragmentDirections.actionOrderFragmentToOrderCategoryUpdateDialog(orderCategory.categoryId, orderCategory.categoryName, orderCategory.color)
        navController.navigate(action)
    }

    private fun navigateToDeleteDialog(orderCategory: OrderCategoryDTO) {
        val action = OrderFragmentDirections.actionOrderFragmentToOrderCategoryDeleteDialog(orderCategory.categoryId)
        navController.navigate(action)
    }
}