package com.kitchingapp.view.fragment.recipe

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentRecipeBinding
import com.kitchingapp.adapter.RecipeRecycleAdapter
import com.kitchingapp.common.KitchingApplication
import com.kitchingapp.data.database.dto.RecipeDetailDTO
import com.kitchingapp.view.model.RecipeViewModel
import com.kitchingapp.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecipeFragment : BaseFragment<FragmentRecipeBinding>(FragmentRecipeBinding::inflate){
    private lateinit var navController: NavController
    private val viewModel by viewModels<RecipeViewModel> {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipeList.collectLatest {
                    notifyRecipe(it)
                }
            }
        }
        viewModel.getRecipeList(teamId = "3uM01g5GSz8lC49JA6vq")
    }

    private fun notifyRecipe(recipe: List<RecipeDetailDTO>) {
        with(binding.recipeRV) {
            layoutManager = GridLayoutManager(KitchingApplication.getAppContext(), 2)
            val recipeAdapter = RecipeRecycleAdapter(viewLifecycleOwner, navController)
            recipeAdapter.submitList(recipe)
            this.adapter = recipeAdapter
        }
    }
}