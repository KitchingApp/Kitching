package com.kitching.data.repository

import com.kitching.data.datasource.StaffLevelDataSourceImpl
import com.kitching.data.dto.StaffLevelDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.domain.datasource.StaffLevelDataSource
import com.kitching.domain.repository.StaffLevelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class StaffLevelRepositoryImpl(
    private val staffLevelDataSource: StaffLevelDataSource = StaffLevelDataSourceImpl()
) : StaffLevelRepository {
    override fun getStaffLevels(departmentId: String): Flow<FirebaseResult<List<StaffLevelDTO>>> =
        flow {
            emit(FirebaseResult.Loading)
            val staffLevels = staffLevelDataSource.getStaffLevels(departmentId)
            if (staffLevels.isEmpty()) emit(FirebaseResult.Success(emptyList()))
            else emit(FirebaseResult.Success(staffLevels.map {
                StaffLevelDTO(
                    staffLevelId = it.id,
                    staffLevelName = it.name
                )
            }))
        }.catch {
            emit(FirebaseResult.Failure(it))
        }

    override fun createStaffLevel(
        departmentId: String,
        name: String
    ): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = staffLevelDataSource.createStaffLevel(departmentId, name)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun updateStaffLevel(
        staffLevelId: String,
        name: String
    ): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = staffLevelDataSource.updateStaffLevel(staffLevelId, name)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun deleteStaffLevel(staffLevelId: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = staffLevelDataSource.deleteStaffLevel(staffLevelId)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }
}