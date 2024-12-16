package com.kitchingapp.view.fragment.recipe

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentRecipeBinding
import com.kitchingapp.R
import com.kitchingapp.adapter.RecipeRecycleAdapter
import com.kitchingapp.common.KitchingApplication

class RecipeFragment : BaseFragment<FragmentRecipeBinding>(FragmentRecipeBinding::inflate){
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeDate = RestaurantGenerator.restaurantList().flatMap { it.recipes }

        with(binding.recipeRV) {
            layoutManager = GridLayoutManager(KitchingApplication.getAppContext(), 2)
            val recipeAdapter = RecipeRecycleAdapter(viewLifecycleOwner, navController)
            recipeAdapter.submitList(recipeDate)
            this.adapter = recipeAdapter
        }
    }
}