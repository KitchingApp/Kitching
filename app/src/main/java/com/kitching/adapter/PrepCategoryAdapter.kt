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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.R
import com.kitching.common.KitchingApplication
import com.kitching.common.util.throttleFirst
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.dto.PrepCategoryDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.databinding.ItemBigCategoryBinding
import com.kitching.view.fragment.prep.PrepCategoryFragmentDirections
import com.kitching.view.model.PrepViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.appcompat.itemClicks
import ru.ldralighieri.corbind.view.clicks

class PrepCategoryAdapter(
    private val lifecycleOwner: LifecycleOwner
) : ListAdapter<PrepCategoryDTO, PrepCategoryAdapter.PrepCategoryViewHolder>(diffUtil),
    ViewModelProvider.Factory {

    private val viewModel = PrepViewModel.instance

    private var navController: NavController? = null

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrepCategoryViewHolder {
        context = parent.context
        val binding =
            ItemBigCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        navController = Navigation.findNavController(parent)
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
                return oldItem.categoryName == newItem.categoryName && oldItem.color == newItem.color
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
                    navController?.navigate(argActions)
                }.launchIn(lifecycleOwner.lifecycleScope)
                optionBtn.clicks().throttleFirst().onEach {
                    showMenu(optionBtn, R.menu.option_menu, prepCategory.categoryId)
                }.launchIn(lifecycleOwner.lifecycleScope)
            }
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int, categoryId: String) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.itemClicks().onEach {
            val teamId = PreferencesDataSource(KitchingApplication.getAppContext()).getTeamId() ?: ""
            when(it.itemId) {
                R.id.updateInOptionMenu -> {
//                    updateItem(teamId, categoryId)
                }
                R.id.deleteInOptionMenu -> {
                    val action = PrepCategoryFragmentDirections.actionPrepFragmentToPrepCategoryDeleteDialog(categoryId)
                    navController?.navigate(action)
                }
            }
        }.launchIn(lifecycleOwner.lifecycleScope)

        popup.show()
    }

    private fun deleteItem(teamId: String, categoryId: String) {
        viewModel.deletePrepCategory(categoryId)
        lifecycleOwner.lifecycleScope.launch {
            viewModel.deletePrepCategoryResult.collectLatest {
                when(it) {
                    is FirebaseResult.Success -> {
                        viewModel.getPrepCategory(teamId)
                    }
                    FirebaseResult.DummyConstructor -> {
//                        TODO()
                    }
                    is FirebaseResult.Failure -> {
//                        TODO()
                    }
                    FirebaseResult.Loading -> {
//                        TODO()
                    }
                }
            }
        }
    }

    private fun updateItem(teamId: String, categoryId: String, categoryName: String, color: String) {
        viewModel.updatePrepCategory(categoryId, categoryName, color)
        lifecycleOwner.lifecycleScope.launch {
            viewModel.deletePrepCategoryResult.collectLatest {
                when(it) {
                    is FirebaseResult.Success -> {
                        viewModel.getPrepCategory(teamId)
                    }
                    FirebaseResult.DummyConstructor -> {
//                        TODO()
                    }
                    is FirebaseResult.Failure -> {
//                        TODO()
                    }
                    FirebaseResult.Loading -> {
//                        TODO()
                    }
                }
            }
        }
    }
}