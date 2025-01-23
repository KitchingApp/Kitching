package com.kitching.domain.datasource

import android.net.Uri
import com.kitching.domain.entities.Recipe

interface RecipeDataSource {
    suspend fun getRecipeList(teamId: String): List<Recipe>

    /** return: imageUrl */
    suspend fun uploadImageToStorage(imageUri: Uri, imageName: String): String

    /** return: recipeId */
    suspend fun saveRecipe(
        name: String,
        picture: String,
        steps: List<String>,
        teamId: String
    ): String

    suspend fun saveIngredients(recipeId: String, ingredients: List<Map<String, String>>): Boolean
}