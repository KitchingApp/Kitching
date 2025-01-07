package com.kitching.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.R
import com.kitching.common.util.throttleClicks
import com.kitching.common.util.throttleFirst
import com.kitching.data.dto.PrepDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.databinding.ItemSmallCategoryBinding
import com.kitching.view.fragment.prep.PrepCategoryFragmentDirections
import com.kitching.view.fragment.prep.PrepListFragmentDirections
import com.kitching.view.model.PrepViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.appcompat.itemClicks
import ru.ldralighieri.corbind.view.clicks

class PrepAdapter(private val lifecycleOwner: LifecycleOwner): ListAdapter<PrepDTO, PrepAdapter.PrepViewHolder>(diffUtil) {

    private val viewModel = PrepViewModel.instance

    private var navController: NavController? = null

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrepViewHolder {
        context = parent.context
        val binding = ItemSmallCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        navController = Navigation.findNavController(parent)
        return PrepViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PrepViewHolder, position: Int) {
        holder.bindItem(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PrepDTO>() {
            override fun areItemsTheSame(
                oldItem: PrepDTO,
                newItem: PrepDTO
            ): Boolean {
                return oldItem.prepName == newItem.prepName
            }

            override fun areContentsTheSame(
                oldItem: PrepDTO,
                newItem: PrepDTO
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class PrepViewHolder(val binding: ItemSmallCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(prep: PrepDTO) {
            with(binding) {
                categoryNameTV.text = prep.prepName
                optionBtn.clicks().throttleFirst().onEach {
                    showMenu(optionBtn, R.menu.option_menu, prep.categoryId, prep.prepId, prep.prepName)
                }.launchIn(lifecycleOwner.lifecycleScope)
            }
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int, categoryId: String, prepId: String, name: String) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.itemClicks().onEach {
            when(it.itemId) {
                R.id.updateInOptionMenu -> {
                    val action = PrepListFragmentDirections.actionPrepListFragmentToPrepUpdateDialog(categoryId, prepId, name)
                    navController?.navigate(action)
                }
                R.id.deleteInOptionMenu -> {
                    val action = PrepListFragmentDirections.actionPrepListFragmentToPrepDeleteDialog(categoryId = categoryId, prepId = prepId)
                    navController?.navigate(action)
                }
            }
        }.launchIn(lifecycleOwner.lifecycleScope)

        popup.show()
    }
}