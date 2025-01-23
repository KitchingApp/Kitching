package com.kitching.domain.datasource

import com.kitching.domain.entities.ScheduleTime

interface ScheduleTimeDataSource {
    suspend fun getScheduleTime(scheduleTimeId: String): ScheduleTime?

    suspend fun getScheduleTimes(teamId: String): List<ScheduleTime>

    suspend fun createScheduleTime(teamId: String, name: String, startTime: String, endTime: String, color: String): Boolean

    suspend fun updateScheduleTime(scheduleTimeId: String, name: String, startTime: String, endTime: String, color: String): Boolean

    suspend fun deleteScheduleTime(scheduleTimeId: String): Boolean
}