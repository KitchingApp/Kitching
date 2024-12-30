package com.kitching.data.repository

import com.kitching.data.dto.IngredientDTO
import com.kitching.data.dto.RecipeDetailDTO
import com.kitching.data.firebase.FireStoreDataSource
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecipeRepository(private val dataSource: FireStoreDataSource = FireStoreDataSource()) {
    suspend fun getRecipeList(teamId: String): Flow<FirebaseResult<MutableList<RecipeDetailDTO>>> = flow{
        emit(FirebaseResult.Loading)
        runCatching {
            val recipeDTOList = mutableListOf<RecipeDetailDTO>()
            dataSource.getRecipeList(teamId).forEach {
                val ingredientDTOList = mutableListOf<IngredientDTO>()
                it.ingredients.forEach { ingredient ->
                    val ingredientDTO = IngredientDTO(
                        ingredientId = ingredient.id,
                        ingredientName = ingredient.name,
                        once = ingredient.once,
                        twice = ingredient.twice,
                        each = ingredient.each
                    )
                    ingredientDTOList.add(ingredientDTO)
                }
                val recipeDTO = RecipeDetailDTO(
                    recipeId = it.id,
                    recipeName = it.name,
                    picture = it.picture,
                    ingredients = ingredientDTOList,
                    steps = it.steps
                )
                recipeDTOList.add(recipeDTO)
            }
            recipeDTOList
        }
            .onSuccess { emit(FirebaseResult.Success(it)) }
            .onFailure { emit(FirebaseResult.Failure(it)) }
    }
}