package com.kitching.domain.repository

import com.kitching.data.dto.PrepCategoryDTO
import com.kitching.data.dto.PrepDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface PrepRepository {
    /** PrepCategory */
    suspend fun getPrepCategory(teamId: String): Flow<FirebaseResult<MutableList<PrepCategoryDTO>>>

    suspend fun createPrepCategory(teamId: String, categoryName: String, color: String): Flow<FirebaseResult<Boolean>>

    suspend fun updatePrepCategory(categoryId: String, categoryName: String, color: String): Flow<FirebaseResult<Boolean>>

    suspend fun deletePrepCategory(scheduleId: String): Flow<FirebaseResult<Boolean>>

    /** PrepList */
    suspend fun getPrepList(categoryId: String): Flow<FirebaseResult<MutableList<PrepDTO>>>

    suspend fun createPrep(categoryId: String, name: String): Flow<FirebaseResult<Boolean>>

    suspend fun updatePrep(prepId: String, name: String): Flow<FirebaseResult<Boolean>>

    suspend fun deletePrep(prepId: String): Flow<FirebaseResult<Boolean>>
}