package com.kitching.data.repository

import com.kitching.data.datasource.DepartmentDataSourceImpl
import com.kitching.data.dto.DepartmentDTO
import com.kitching.data.dto.StaffLevelDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.domain.datasource.DepartmentDataSource
import com.kitching.domain.repository.DepartmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class DepartmentRepositoryImpl(private val dataSource: DepartmentDataSource = DepartmentDataSourceImpl()): DepartmentRepository {
    override fun getDepartments(teamId: String): Flow<FirebaseResult<List<DepartmentDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val departments = dataSource.getDepartments(teamId).getOrThrow().map {
            DepartmentDTO(
                departmentId = it.id,
                departmentName = it.name,
                color = it.color
            )
        }
        emit(FirebaseResult.Success(departments))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun createDepartment(
        teamId: String,
        name: String,
        color: String
    ): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = dataSource.createDepartment(teamId, name, color)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun updateDepartment(
        departmentId: String,
        name: String,
        color: String
    ): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = dataSource.updateDepartment(departmentId, name, color)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun deleteDepartment(departmentId: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = dataSource.deleteDepartment(departmentId)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun getStaffLevels(departmentId: String): Flow<FirebaseResult<List<StaffLevelDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val staffLevels = dataSource.getStaffLevels(departmentId).getOrThrow().map {
            StaffLevelDTO(
                staffLevelId = it.id,
                staffLevelName = it.name
            )
        }
        emit(FirebaseResult.Success(staffLevels))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun createStaffLevel(
        departmentId: String,
        name: String
    ): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = dataSource.createStaffLevel(departmentId, name)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun updateStaffLevel(
        staffLevelId: String,
        name: String
    ): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = dataSource.updateStaffLevel(staffLevelId, name)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun deleteStaffLevel(staffLevelId: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = dataSource.deleteStaffLevel(staffLevelId)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }
}