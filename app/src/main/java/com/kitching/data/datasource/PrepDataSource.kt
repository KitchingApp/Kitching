package com.kitching.data.datasource

import com.kitching.domain.entities.Prep
import com.kitching.domain.entities.PrepCategory

interface PrepDataSource {
    /** PrepCategory */
    suspend fun prepCategoryCreate(teamId: String, categoryName: String, color: String): Boolean

    suspend fun prepCategoryRead(teamId: String): Result<List<PrepCategory>>

    suspend fun prepCategoryUpdate(categoryId: String, categoryName: String, color: String): Boolean

    suspend fun prepCategoryDelete(prepCategoryId: String): Boolean

    /** PrepList */
    suspend fun prepListCreate(categoryId: String, name: String): Boolean

    suspend fun prepListRead(categoryId: String): Result<List<Prep>>

    suspend fun prepListUpdate(prepId: String, name: String): Boolean

    suspend fun prepListDelete(prepId: String): Boolean
}