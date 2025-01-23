package com.kitching.data.datasource

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kitching.common.COLLECTION_INGREDIENT
import com.kitching.common.COLLECTION_RECIPE
import com.kitching.domain.datasource.RecipeDataSource
import com.kitching.domain.entities.Ingredient
import com.kitching.domain.entities.Recipe
import kotlinx.coroutines.tasks.await

class RecipeDataSourceImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) : RecipeDataSource {
    override suspend fun getRecipeList(teamId: String): List<Recipe> {
        return db.collection(COLLECTION_RECIPE).get()
            .await().documents.mapNotNull { documentSnapshot ->
                val recipe = documentSnapshot.toObject(Recipe::class.java) ?: return@mapNotNull null
                val ingredients =
                    documentSnapshot.reference.collection(COLLECTION_INGREDIENT).get().await()
                        .toObjects(Ingredient::class.java)

                recipe.copy(ingredients = ingredients)
            }
    }

    override suspend fun uploadImageToStorage(
        imageUri: Uri,
        imageName: String,
    ): String {
        val storageRef = storage.reference.child("recipeImage/$imageName")
        storageRef.putFile(imageUri).await()
        return storageRef.downloadUrl.await().toString()
    }

    override suspend fun saveRecipe(
        name: String,
        picture: String,
        steps: List<String>,
        teamId: String,
    ): String {
        val recipeData = mapOf(
            "id" to "",
            "name" to name,
            "picture" to picture,
            "steps" to steps,
            "teamId" to teamId
        )
        val recipeDocument = db.collection("recipe").add(recipeData).await()
        db.collection("recipe").document(recipeDocument.id).update("id", recipeDocument.id)
            .await()
        return recipeDocument.id
    }

    override suspend fun saveIngredients(
        recipeId: String,
        ingredients: List<Map<String, String>>,
    ): Boolean {
        return runCatching {
            val ingredientCollection =
                db.collection("recipe").document(recipeId).collection("ingredient")
            ingredients.forEach { ingredient ->
                // MutableMap<String, Any>로 변환
                val ingredientData = ingredient.mapValues { (key, value) ->
                    when (key) {
                        "once", "twice" -> value.toIntOrNull() ?: 0
                        else -> value
                    }
                }

                // Firestore에 데이터 추가 및 업데이트
                val ingredientDocument = ingredientCollection.add(ingredientData).await()
                ingredientCollection.document(ingredientDocument.id)
                    .update("id", ingredientDocument.id).await()
            }
        }.isSuccess
    }

}