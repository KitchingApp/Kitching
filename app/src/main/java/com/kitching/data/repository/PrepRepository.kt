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

    suspend fun createPrepCategory(teamId: String, categoryName: String, color: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.createPrepCategory(teamId, categoryName, color))
    }

    suspend fun updatePrepCategory(categoryId: String, categoryName: String, color: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.updatePrepCategory(categoryId, categoryName, color))
    }

    suspend fun deletePrepCategory(scheduleId: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.deletePrepCategory(scheduleId))
    }

    suspend fun getPrepList(categoryId: String): Flow<FirebaseResult<MutableList<PrepDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getPrepList(categoryId) },
            mapper = {
                PrepDTO(
                    it.categoryId,
                    it.id,
                    it.name,
                    it.recipeId,
                    it.recipeId?.let { id -> dataSource.getRecipeName(id) })
            })
    }

    suspend fun createPrep(categoryId: String, name: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.createPrep(categoryId, name))
    }

    suspend fun updatePrep(prepId: String, name: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.updatePrep(prepId, name))
    }

    suspend fun deletePrep(prepId: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.deletePrep(prepId))
    }
}