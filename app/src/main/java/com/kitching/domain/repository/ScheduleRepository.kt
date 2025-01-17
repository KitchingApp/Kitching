package com.kitching.domain.repository

import com.kitching.data.dto.DropDownDepartmentsDTO
import com.kitching.data.dto.ScheduleDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getDepartments(teamId: String): Flow<FirebaseResult<List<DropDownDepartmentsDTO>>>

    fun getSchedules(teamId: String, date: String): Flow<FirebaseResult<List<ScheduleDTO>>>
}