package com.kitching.data.repository

import com.kitching.data.dto.PrepCategoryDTO
import com.kitching.data.dto.PrepDTO
import com.kitching.data.firebase.FireStoreDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.firebase.fetchFirebaseDataFlow
import kotlinx.coroutines.flow.Flow

class PrepRepository(private val dataSource: FireStoreDataSource = FireStoreDataSource()) {
    suspend fun getPrepCategory(teamId: String): Flow<FirebaseResult<MutableList<PrepCategoryDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getPrepCategory(teamId) },
            mapper = {
                PrepCategoryDTO(
                    categoryId = it.id,
                    categoryName = it.name,
                    color = it.color
                )
            }
        )
    }

    suspend fun getPrepList(categoryId: String): Flow<FirebaseResult<MutableList<PrepDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getPrepList(categoryId) },
            mapper = {
                PrepDTO(
                    it.id,
                    it.name,
                    it.recipeId,
                    it.recipeId?.let { id -> dataSource.getRecipeName(id) })
            })
    }
}