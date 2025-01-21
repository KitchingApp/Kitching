package com.kitching.domain.repository

import com.kitching.data.dto.DepartmentDTO
import com.kitching.data.dto.DropDownDepartmentsDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface DepartmentRepository {
    fun getDepartments(teamId: String): Flow<FirebaseResult<List<DepartmentDTO>>>

    fun getDepartmentsForDropdown(teamId: String): Flow<FirebaseResult<List<DropDownDepartmentsDTO>>>

    fun createDepartment(teamId: String, name: String, color: String): Flow<FirebaseResult<Boolean>>

    fun updateDepartment(departmentId: String, name: String, color: String): Flow<FirebaseResult<Boolean>>

    fun deleteDepartment(departmentId: String): Flow<FirebaseResult<Boolean>>
}