package com.kitching.data.dto

data class PrepCategoryDTO(val categoryId: String, val categoryName: String, val color: String)

data class PrepDTO(val prepId: String, val prepName: String, val recipeId: String?, val recipeName: String?)
