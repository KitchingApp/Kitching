package com.kitching.domain.datasource

import com.kitching.domain.entities.Prep
import com.kitching.domain.entities.PrepCategory

interface PrepDataSource {
    suspend fun getPrepList(categoryId: String): Result<List<Prep>>

    suspend fun createPrepList(categoryId: String, name: String): Boolean

    suspend fun updatePrepList(prepId: String, name: String): Boolean

    suspend fun deletePrepList(prepId: String): Boolean
}