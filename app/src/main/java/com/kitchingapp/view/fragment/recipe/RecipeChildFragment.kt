package com.kitchingapp.view.fragment.recipe

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.kitchingapp.adapter.RecipeRecycleAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.ChildfragmentRecipeBinding

class RecipeChildFragment: BaseFragment<ChildfragmentRecipeBinding>(ChildfragmentRecipeBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allRecipes = RestaurantGenerator.restaurantList().flatMap { it.recipes }

        with(binding.recipeRV) {
            layoutManager = GridLayoutManager(context, 2)
            val adapter = RecipeRecycleAdapter()
            adapter.submitList(allRecipes)
            this.adapter = adapter
        }
    }
}