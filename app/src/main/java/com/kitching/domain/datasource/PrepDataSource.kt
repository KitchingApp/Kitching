package com.kitching.domain.datasource

import com.kitching.domain.entities.Prep
import com.kitching.domain.entities.PrepCategory

interface PrepDataSource {
    /** PrepCategory */
    suspend fun createPrepCategory(teamId: String, categoryName: String, color: String): Boolean

    suspend fun getPrepCategory(teamId: String): List<PrepCategory>

    suspend fun updatePrepCategory(categoryId: String, categoryName: String, color: String): Boolean

    suspend fun deletePrepCategory(prepCategoryId: String): Boolean

    /** PrepList */
    suspend fun createPrepList(categoryId: String, name: String): Boolean

    suspend fun getPrepList(categoryId: String): List<Prep>

    suspend fun updatePrepList(prepId: String, name: String): Boolean

    suspend fun deletePrepList(prepId: String): Boolean
}