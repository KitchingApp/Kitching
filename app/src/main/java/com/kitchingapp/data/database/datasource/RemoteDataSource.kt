package com.kitchingapp.data.database.datasource

import com.kitchingapp.domain.entities.Schedule
import com.kitchingapp.domain.entities.Team
import java.time.LocalDate

interface RemoteDataSource {
    suspend fun getTeams(userId: String): List<Team>

    suspend fun getTeamSchedules(teamId: String, date: String): List<Schedule>

    suspend fun getScheduleTimeName(teamId: String, scheduleTimeId: String): String

    suspend fun getDepartmentId(teamId: String, userId: String): String

    suspend fun getDepartmentName(teamId: String, departmentId: String): String

    suspend fun getUserName(userId: String): String

    suspend fun createSchedule(schedule: Schedule): Boolean

    suspend fun deleteSchedule(scheduleId: String): Boolean
}