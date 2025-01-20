package com.kitching.domain.repository

import com.kitching.data.dto.ScheduleTimeListDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface ScheduleTimeRepository {
    fun getScheduleTimes(teamId: String): Flow<FirebaseResult<List<ScheduleTimeListDTO>>>

    fun createScheduleTime(teamId: String, name: String, color: String, startTime: String, endTime: String): Flow<FirebaseResult<Boolean>>

    fun updateScheduleTime(scheduleTimeId: String, name: String, color: String, startTime: String, endTime: String): Flow<FirebaseResult<Boolean>>

    fun deleteScheduleTime(scheduleTimeId: String): Flow<FirebaseResult<Boolean>>
}