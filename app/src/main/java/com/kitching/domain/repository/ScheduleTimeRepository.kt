package com.kitching.domain.repository

import com.kitching.data.dto.ScheduleTimeChipsDTO
import com.kitching.data.dto.ScheduleTimeListDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface ScheduleTimeRepository {
    fun getScheduleTimes(teamId: String): Flow<FirebaseResult<List<ScheduleTimeListDTO>>>

    fun getScheduleTimesForChips(teamId: String): Flow<FirebaseResult<List<ScheduleTimeChipsDTO>>>

    fun createScheduleTime(teamId: String, name: String, color: String, startTime: String, endTime: String): Flow<FirebaseResult<Boolean>>

    fun updateScheduleTime(scheduleTimeId: String, name: String, color: String, startTime: String, endTime: String): Flow<FirebaseResult<Boolean>>

    fun deleteScheduleTime(scheduleTimeId: String): Flow<FirebaseResult<Boolean>>
}