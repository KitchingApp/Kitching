package com.kitching.domain.repository

import com.kitching.data.dto.PrepDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface PrepRepository {
    fun getPrepList(categoryId: String): Flow<FirebaseResult<List<PrepDTO>>>

    fun createPrep(categoryId: String, name: String): Flow<FirebaseResult<Boolean>>

    fun updatePrep(prepId: String, name: String): Flow<FirebaseResult<Boolean>>

    fun deletePrep(prepId: String): Flow<FirebaseResult<Boolean>>
}