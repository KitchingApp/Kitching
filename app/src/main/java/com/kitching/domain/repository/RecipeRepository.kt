package com.kitching.domain.repository

import android.net.Uri
import com.kitching.data.dto.RecipeDetailDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun getRecipeList(teamId: String): Flow<FirebaseResult<MutableList<RecipeDetailDTO>>>

    suspend fun uploadImage(imageUri: Uri, imageName: String): Flow<FirebaseResult<String>>

    suspend fun saveRecipe(
        name: String,
        picture: String,
        steps: List<String>,
        teamId: String
    ): Flow<FirebaseResult<String>>

    suspend fun saveIngredients(recipeId: String, ingredients: List<Map<String, String>>): Flow<FirebaseResult<Boolean>>
}