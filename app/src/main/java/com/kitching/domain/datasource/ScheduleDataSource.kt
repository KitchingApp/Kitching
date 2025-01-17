package com.kitching.domain.datasource

import com.kitching.domain.entities.Department
import com.kitching.domain.entities.ScheduleInfo
import com.kitching.domain.entities.ScheduleTime
import com.kitching.domain.entities.User

interface ScheduleDataSource {
    suspend fun getDepartments(teamId: String): Result<List<Department>>

    suspend fun getScheduleInfos(teamId: String, date: String): Result<List<ScheduleInfo>>

    suspend fun getMembers(teamId: String): Result<List<User>>

    suspend fun getScheduleTimes(teamId: String): Result<List<ScheduleTime>>

    suspend fun createSchedule(teamId: String, dateString: String, userId: String, scheduleTimeId: String, isFix: Boolean = true): Boolean

    suspend fun deleteSchedule(scheduleId: String): Boolean

    suspend fun applySchedule(scheduleId: String): Boolean
}