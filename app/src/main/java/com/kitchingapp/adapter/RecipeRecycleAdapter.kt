package com.kitchingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.databinding.ItemRecipeBinding
import com.kitchingapp.domain.entities.Recipe
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import com.kitchingapp.view.fragment.recipe.RecipeFragmentDirections
import kotlinx.coroutines.flow.launchIn

class RecipeRecycleAdapter(private val lifecycleOwner: LifecycleOwner, private val navController: NavController) : ListAdapter<Recipe, RecipeRecycleAdapter.RecipeViewHolder>(diffUtil) {
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
        val diffUtil = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(
                oldItem: Recipe,
                newItem: Recipe
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: Recipe,
                newItem: Recipe
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class RecipeViewHolder(val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun  bindRecipe(recipe: Recipe) {
                with(binding) {
                    recipeIV.setImageResource(recipe.foodPicture)
                    recipeNameTV.text = recipe.name
                    recipeCV.clicks().onEach {
                        val argsAction = RecipeFragmentDirections.actionRecipeFragmentToRecipeDetailFragment(recipe.foodPicture, recipe.name,
                            recipe.steps.toString()
                        )
                        navController.navigate(argsAction)
                    }.launchIn(lifecycleOwner.lifecycleScope)
                }
            }
        }
}