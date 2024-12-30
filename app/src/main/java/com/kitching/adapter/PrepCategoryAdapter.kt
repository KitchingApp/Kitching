package com.kitching.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.R
import com.kitching.common.throttleFirst
import com.kitching.data.dto.PrepCategoryDTO
import com.kitching.databinding.ItemBigCategoryBinding
import com.kitching.view.fragment.prep.PrepCategoryFragmentDirections
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class PrepCategoryAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val navController: NavController
) : ListAdapter<PrepCategoryDTO, PrepCategoryAdapter.PrepCategoryViewHolder>(diffUtil),
    ViewModelProvider.Factory {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrepCategoryViewHolder {
        context = parent.context
        val binding =
            ItemBigCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PrepCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PrepCategoryViewHolder, position: Int) {
        holder.bindBigCategory(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PrepCategoryDTO>() {
            override fun areItemsTheSame(
                oldItem: PrepCategoryDTO,
                newItem: PrepCategoryDTO
            ): Boolean {
                return oldItem.categoryName == newItem.categoryName
            }

            override fun areContentsTheSame(
                oldItem: PrepCategoryDTO,
                newItem: PrepCategoryDTO
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class PrepCategoryViewHolder(val binding: ItemBigCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindBigCategory(prepCategory: PrepCategoryDTO) {
            with(binding) {
                categoryNameTV.text = prepCategory.categoryName
                categoryCV.setCardBackgroundColor(Color.parseColor(prepCategory.color))
                categoryCV.clicks().throttleFirst().onEach {
                    val argActions = PrepCategoryFragmentDirections.actionPrepFragmentToPrepListFragment(prepCategory.categoryId)
                    navController.navigate(argActions)
                }.launchIn(lifecycleOwner.lifecycleScope)
                optionBtn.clicks().throttleFirst().onEach {
                    showMenu(optionBtn, R.menu.option_menu, adapterPosition)
                }.launchIn(lifecycleOwner.lifecycleScope)
            }
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int, position: Int) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

//        popup.itemClicks().onEach {
//            Log.d("test", "$position 번 $it")
//        }.launchIn(lifecycleOwner.lifecycleScope)

        // Show the popup menu.
        popup.show()
    }

    private fun removeItem(position: Int) {
        val newList = currentList.toMutableList()
        newList.removeAt(position)
        submitList(newList)
    }

    private fun updateItem(position: Int) {
        val item = currentList[position]
        val newList = currentList.toMutableList()
        newList[position] = item.copy(categoryName = "수정된 카테고리입니당당구리구리")
        submitList(newList)
    }
}