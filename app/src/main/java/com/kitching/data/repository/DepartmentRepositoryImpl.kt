package com.kitching.data.repository

import com.kitching.data.datasource.DepartmentDataSourceImpl
import com.kitching.data.dto.DepartmentDTO
import com.kitching.data.dto.DropDownDepartmentsDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.domain.datasource.DepartmentDataSource
import com.kitching.domain.repository.DepartmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class DepartmentRepositoryImpl(
    private val departmentDataSource: DepartmentDataSource = DepartmentDataSourceImpl(),
): DepartmentRepository {
    override fun getDepartments(teamId: String): Flow<FirebaseResult<List<DepartmentDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val departments = departmentDataSource.getDepartments(teamId).getOrThrow().map {
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

    override fun getDepartmentsForDropdown(teamId: String): Flow<FirebaseResult<List<DropDownDepartmentsDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val departments = departmentDataSource.getDepartments(teamId).getOrThrow().map {
            DropDownDepartmentsDTO(
                departmentId = it.id,
                departmentName = it.name
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
        val result = departmentDataSource.createDepartment(teamId, name, color)
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
        val result = departmentDataSource.updateDepartment(departmentId, name, color)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun deleteDepartment(departmentId: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = departmentDataSource.deleteDepartment(departmentId)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }
}