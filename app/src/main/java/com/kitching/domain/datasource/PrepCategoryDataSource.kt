package com.kitching.domain.datasource

import com.kitching.domain.entities.Prep
import com.kitching.domain.entities.PrepCategory

interface PrepCategoryDataSource {
    suspend fun getPrepCategory(teamId: String): List<PrepCategory>

    suspend fun createPrepCategory(teamId: String, categoryName: String, color: String): Boolean

    suspend fun updatePrepCategory(categoryId: String, categoryName: String, color: String): Boolean

    suspend fun deletePrepCategory(prepCategoryId: String): Boolean
}