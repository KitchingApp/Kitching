package com.kitching.data.repository

import com.kitching.data.datasource.PrepDataSourceImpl
import com.kitching.data.dto.PrepCategoryDTO
import com.kitching.data.dto.PrepDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.firebase.fetchFirebaseDataFlow
import com.kitching.domain.repository.PrepRepository
import kotlinx.coroutines.flow.Flow

class PrepRepositoryImpl(private val dataSource: PrepDataSourceImpl = PrepDataSourceImpl()): PrepRepository {
    /** PrepCategory */
    override suspend fun getPrepCategory(teamId: String): Flow<FirebaseResult<MutableList<PrepCategoryDTO>>> {
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

    override suspend fun createPrepCategory(teamId: String, categoryName: String, color: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.createPrepCategory(teamId, categoryName, color))
    }

    override suspend fun updatePrepCategory(categoryId: String, categoryName: String, color: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.updatePrepCategory(categoryId, categoryName, color))
    }

    override suspend fun deletePrepCategory(scheduleId: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.deletePrepCategory(scheduleId))
    }

    /** PrepList */
    override suspend fun getPrepList(categoryId: String): Flow<FirebaseResult<MutableList<PrepDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getPrepList(categoryId) },
            mapper = {
                PrepDTO(
                    it.categoryId,
                    it.id,
                    it.name
                )
            })
    }

    override suspend fun createPrep(categoryId: String, name: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.createPrepList(categoryId, name))
    }

    override suspend fun updatePrep(prepId: String, name: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.updatePrepList(prepId, name))
    }

    override suspend fun deletePrep(prepId: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.deletePrepList(prepId))
    }
}