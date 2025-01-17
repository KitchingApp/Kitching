package com.kitching.adapter

import android.content.Context
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
import com.kitching.data.dto.OrderDTO
import com.kitching.databinding.ItemSmallCategoryBinding
import com.kitching.R
import com.kitching.view.fragment.order.OrderListFragmentDirections
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.widget.itemClicks

class OrderListAdapter(private val lifecycleOwner: LifecycleOwner, private val navController: NavController): ListAdapter<OrderDTO, OrderListAdapter.OrderViewHolder>(diffUtil) {
    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderViewHolder {
        context = parent.context

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

                optionBtn.throttleClicks(lifecycleOwner) {
                    showMenu(optionBtn, order)
                }
            }
        }
    }

    private fun showMenu(anchor: View, order: OrderDTO) {
        val popup = PopupMenu(context, anchor)
        popup.menuInflater.inflate(R.menu.option_menu, popup.menu)

        popup.itemClicks().onEach {
            when(it.itemId) {
                R.id.updateInOptionMenu -> navigateToUpdateDialog(order)
                R.id.deleteInOptionMenu -> navigateToDeleteDialog(order)
            }
        }.launchIn(lifecycleOwner.lifecycleScope)

        popup.show()
    }

    private fun navigateToUpdateDialog(order: OrderDTO) {
        val action = OrderListFragmentDirections.actionOrderListFragmentToOrderUpdateDialog(order.categoryId, order.orderId, order.orderName)
        navController.navigate(action)
    }

    private fun navigateToDeleteDialog(order: OrderDTO) {
        val action = OrderListFragmentDirections.actionOrderListFragmentToOrderDeleteDialog(order.categoryId, order.orderId)
        navController.navigate(action)
    }
}