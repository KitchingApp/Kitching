package com.kitchingapp.view.fragment.recipe

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentRecipeDetailBinding

class RecipeDetailFragment: BaseFragment<FragmentRecipeDetailBinding>(FragmentRecipeDetailBinding::inflate) {
    private lateinit var navController: NavController
    private val args: RecipeDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            recipeIV.setImageResource(args.recipeImage)
            recipeNameTV.text = args.recipeName
            recipeOrderTV1.text = args.recipeStep
        }
    }
}