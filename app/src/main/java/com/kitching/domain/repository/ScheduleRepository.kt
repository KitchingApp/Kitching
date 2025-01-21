package com.kitching.domain.repository

import com.kitching.data.dto.DropDownDepartmentsDTO
import com.kitching.data.dto.DropDownMembersDTO
import com.kitching.data.dto.ScheduleDTO
import com.kitching.data.dto.ScheduleTimeChipsDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getSchedules(teamId: String, date: String): Flow<FirebaseResult<List<ScheduleDTO>>>

    fun createSchedule(
        teamId: String,
        dateString: String,
        userId: String,
        scheduleTimeId: String,
        isFix: Boolean = true
    ): Flow<FirebaseResult<Boolean>>

    fun deleteSchedule(scheduleId: String): Flow<FirebaseResult<Boolean>>

    fun applySchedule(scheduleId: String): Flow<FirebaseResult<Boolean>>
}