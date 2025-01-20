package com.kitching.data.datasource

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kitching.common.COLLECTION_RECIPE
import com.kitching.domain.datasource.RecipeDataSource
import com.kitching.domain.entities.Ingredient
import com.kitching.domain.entities.Recipe
import kotlinx.coroutines.tasks.await

class RecipeDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()): RecipeDataSource {
    override suspend fun getRecipeList(teamId: String): MutableList<Recipe> {
        val recipeListSnapshot = db.collection(COLLECTION_RECIPE)
            .whereEqualTo("teamId", teamId)
            .get()
            .await()

        val recipes = mutableListOf<Recipe>()

        for (document in recipeListSnapshot.documents) {
            // 기본 Recipe 필드 가져오기
            val recipe = document.toObject(Recipe::class.java) ?: continue

            // 하위 컬렉션 'ingredient' 가져오기
            val ingredientSnapshot = document.reference.collection("ingredient").get().await()
            val ingredients = ingredientSnapshot.toObjects(Ingredient::class.java)

            // Recipe에 하위 컬렉션 데이터를 추가
            recipes.add(
                recipe.copy(ingredients = ingredients) // 데이터 클래스 복사로 값 설정
            )
        }
        return recipes
    }

    // 파이어베이스 스토리지
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    override suspend fun uploadImageToStorage(
        imageUri: Uri,
        imageName: String,
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun saveRecipe(
        name: String,
        picture: String,
        steps: List<String>,
        teamId: String,
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun saveIngredients(
        recipeId: String,
        ingredients: List<Map<String, String>>,
    ): Boolean {
        TODO("Not yet implemented")
    }

}