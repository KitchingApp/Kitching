package com.kitching.domain.repository

import com.kitching.data.dto.PrepCategoryDTO
import com.kitching.data.dto.PrepDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface PrepCategoryRepository {
    suspend fun getPrepCategory(teamId: String): Flow<FirebaseResult<List<PrepCategoryDTO>>>

    suspend fun createPrepCategory(teamId: String, categoryName: String, color: String): Flow<FirebaseResult<Boolean>>

    suspend fun updatePrepCategory(categoryId: String, categoryName: String, color: String): Flow<FirebaseResult<Boolean>>

    suspend fun deletePrepCategory(scheduleId: String): Flow<FirebaseResult<Boolean>>
}