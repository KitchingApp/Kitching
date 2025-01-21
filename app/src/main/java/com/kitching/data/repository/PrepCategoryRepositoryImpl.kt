package com.kitching.data.repository

import com.kitching.data.datasource.PrepCategoryDataSourceImpl
import com.kitching.data.datasource.PrepDataSourceImpl
import com.kitching.data.dto.PrepCategoryDTO
import com.kitching.data.dto.PrepDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.firebase.fetchFirebaseDataFlow
import com.kitching.domain.datasource.PrepCategoryDataSource
import com.kitching.domain.repository.PrepCategoryRepository
import com.kitching.domain.repository.PrepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class PrepCategoryRepositoryImpl(
    private val prepCategoryDataSource: PrepCategoryDataSource = PrepCategoryDataSourceImpl(),
): PrepCategoryRepository {
    override suspend fun getPrepCategory(teamId: String): Flow<FirebaseResult<List<PrepCategoryDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val prepCategories = prepCategoryDataSource.getPrepCategory(teamId).getOrThrow().map {
            PrepCategoryDTO(
                categoryId = it.id,
                categoryName = it.name,
                color = it.color
            )
        }
        emit(FirebaseResult.Success(prepCategories))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override suspend fun createPrepCategory(teamId: String, categoryName: String, color: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = prepCategoryDataSource.createPrepCategory(teamId, categoryName, color)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override suspend fun updatePrepCategory(categoryId: String, categoryName: String, color: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = prepCategoryDataSource.updatePrepCategory(categoryId, categoryName, color)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override suspend fun deletePrepCategory(scheduleId: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = prepCategoryDataSource.deletePrepCategory(scheduleId)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }
}