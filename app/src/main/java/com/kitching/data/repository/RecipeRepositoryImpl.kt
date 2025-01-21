package com.kitching.data.repository

import android.net.Uri
import com.kitching.data.dto.IngredientDTO
import com.kitching.data.dto.RecipeDetailDTO
import com.kitching.data.firebase.FireStoreDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.firebase.fetchFirebaseDataFlow
import com.kitching.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class RecipeRepositoryImpl(private val dataSource: FireStoreDataSource = FireStoreDataSource()): RecipeRepository {
    override suspend fun getRecipeList(teamId: String): Flow<FirebaseResult<MutableList<RecipeDetailDTO>>> = flow{
        emit(FirebaseResult.Loading)
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
            emit(FirebaseResult.Success(recipeDTOList))
    }.catch { emit(FirebaseResult.Failure(it)) }

    override suspend fun uploadImage(
        imageUri: Uri,
        imageName: String,
    ): Flow<FirebaseResult<String>> = flow {
        emit(FirebaseResult.Loading)

        val result = dataSource.uploadImageToStorage(imageUri, imageName)

        emit(FirebaseResult.Success(result))
    }.catch { emit(FirebaseResult.Failure(it)) }

    override suspend fun saveRecipe(
        name: String,
        picture: String,
        steps: List<String>,
        teamId: String,
    ): Flow<FirebaseResult<String>> = flow {
        emit(FirebaseResult.Loading)

        val result = dataSource.saveRecipe(name, picture, steps, teamId)

        emit(FirebaseResult.Success(result))
    }.catch { emit(FirebaseResult.Failure(it)) }

    override suspend fun saveIngredients(
        recipeId: String,
        ingredients: List<Map<String, String>>,
    ): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)

        val result = dataSource.saveIngredients(recipeId, ingredients)

        emit(FirebaseResult.Success(result))
    }.catch { emit(FirebaseResult.Failure(it)) }
}