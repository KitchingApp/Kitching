package com.kitching.domain.datasource

import android.net.Uri
import com.kitching.domain.entities.Recipe

interface RecipeDataSource {
    suspend fun getRecipeList(teamId: String): Result<List<Recipe>>

    /** return: imageUrl */
    suspend fun uploadImageToStorage(imageUri: Uri, imageName: String): Result<String>

    /** return: recipeId */
    suspend fun saveRecipe(
        name: String,
        picture: String,
        steps: List<String>,
        teamId: String
    ): Result<String>

    suspend fun saveIngredients(recipeId: String, ingredients: List<Map<String, String>>): Boolean
}