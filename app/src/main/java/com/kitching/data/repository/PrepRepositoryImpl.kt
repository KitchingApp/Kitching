package com.kitching.data.repository

import com.kitching.data.datasource.PrepDataSourceImpl
import com.kitching.data.dto.PrepDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.firebase.fetchFirebaseDataFlow
import com.kitching.domain.repository.PrepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class PrepRepositoryImpl(
    private val prepDataSource: PrepDataSourceImpl = PrepDataSourceImpl()
) : PrepRepository {
    override fun getPrepList(categoryId: String): Flow<FirebaseResult<List<PrepDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val prepList = prepDataSource.getPrepList(categoryId)
        if (prepList.isEmpty()) emit(FirebaseResult.Success(emptyList()))
        else emit(FirebaseResult.Success(prepList.map {
            PrepDTO(
                categoryId = it.categoryId,
                prepId = it.id,
                prepName = it.name
            )
        }))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun createPrep(categoryId: String, name: String): Flow<FirebaseResult<Boolean>> =
        flow {
            emit(FirebaseResult.Loading)
            val result = prepDataSource.createPrepList(categoryId, name)
            emit(FirebaseResult.Success(result))
        }.catch {
            emit(FirebaseResult.Failure(it))
        }

    override fun updatePrep(prepId: String, name: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = prepDataSource.updatePrepList(prepId, name)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun deletePrep(prepId: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = prepDataSource.deletePrepList(prepId)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }
}