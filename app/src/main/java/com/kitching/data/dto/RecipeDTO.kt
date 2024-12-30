package com.kitching.data.dto

data class RecipeDetailDTO(
    val recipeId: String,
    val recipeName: String,
    val picture: String,
    val ingredients: List<IngredientDTO>,
    val steps: List<String>
)

data class IngredientDTO(
    val ingredientId: String,
    val ingredientName: String,
    val once: Int,
    val twice: Int,
    val each: String
)