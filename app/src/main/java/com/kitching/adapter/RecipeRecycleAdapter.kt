package com.kitching.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.data.database.dto.RecipeDetailDTO
import com.kitching.databinding.ItemRecipeBinding
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import com.kitching.view.fragment.recipe.RecipeFragmentDirections
import kotlinx.coroutines.flow.launchIn
import com.kitching.R
import com.kitching.common.throttleFirst

class RecipeRecycleAdapter(private val lifecycleOwner: LifecycleOwner, private val navController: NavController) : ListAdapter<RecipeDetailDTO, RecipeRecycleAdapter.RecipeViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecipeViewHolder,
        position: Int
    ) {
        holder.bindRecipe(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<RecipeDetailDTO>() {
            override fun areItemsTheSame(
                oldItem: RecipeDetailDTO,
                newItem: RecipeDetailDTO
            ): Boolean {
                return oldItem.recipeName == newItem.recipeName
            }

            override fun areContentsTheSame(
                oldItem: RecipeDetailDTO,
                newItem: RecipeDetailDTO
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class RecipeViewHolder(val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun  bindRecipe(recipe: RecipeDetailDTO) {
                with(binding) {
                    val picture = R.drawable.pancake
                    recipeIV.setImageResource(picture)
                    recipeNameTV.text = recipe.recipeName
                    recipeCV.clicks().throttleFirst().onEach {
                        val argsAction = RecipeFragmentDirections.actionRecipeFragmentToRecipeDetailFragment(
                            recipe.picture, recipe.recipeName, recipe.ingredients.joinToString("|").toString(), recipe.steps.joinToString("|").toString()
                        )
                        navController.navigate(argsAction)
                    }.launchIn(lifecycleOwner.lifecycleScope)
                }
            }
        }
}