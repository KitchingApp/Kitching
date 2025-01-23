package com.kitching.domain.datasource

import com.kitching.domain.entities.Schedule

interface ScheduleDataSource {
    suspend fun getSchedules(teamId: String, dateString: String): List<Schedule>

    suspend fun createSchedule(teamId: String, dateString: String, userId: String, scheduleTimeId: String, isFix: Boolean = true): Boolean

    suspend fun deleteSchedule(scheduleId: String): Boolean

    suspend fun applySchedule(scheduleId: String): Boolean
}