package com.kitchingapp.data.database.datasource

import com.kitchingapp.domain.entities.Order
import com.kitchingapp.domain.entities.OrderCategory
import com.kitchingapp.domain.entities.Department
import com.kitchingapp.domain.entities.Prep
import com.kitchingapp.domain.entities.PrepCategory
import com.kitchingapp.domain.entities.Schedule
import com.kitchingapp.domain.entities.ScheduleTime
import com.kitchingapp.domain.entities.Team
import com.kitchingapp.domain.entities.UserTeam

interface RemoteDataSource {
    suspend fun getTeams(userId: String): List<Team>

    suspend fun getTeamName(teamId: String): String

    suspend fun getDepartments(teamId: String): List<Department>

    suspend fun getTeamSchedules(teamId: String, date: String): List<Schedule>

    suspend fun getScheduleTimeName(teamId: String, scheduleTimeId: String): String

    suspend fun getDepartmentId(teamId: String, userId: String): String

    suspend fun getDepartmentName(teamId: String, departmentId: String): String

    suspend fun getStaffLevelId(teamId: String, userId: String): String

    suspend fun getStaffLevelName(staffLevelId: String): String

    suspend fun getUserName(userId: String): String

    suspend fun createSchedule(schedule: Schedule): Boolean

    suspend fun deleteSchedule(scheduleId: String): Boolean

    /** Order Page */

    suspend fun getOrderCategory(teamId: String): MutableList<OrderCategory>

    suspend fun getOrderList(categoryId: String): MutableList<Order>

    /** Prep */

    suspend fun getPrepCategory(teamId: String): MutableList<PrepCategory>

    suspend fun getPrepList(categoryId: String): MutableList<Prep>

    /** other */

    suspend fun getAllMembers(teamId: String): MutableList<UserTeam>

    suspend fun getRecipeName(recipeId: String): String

    suspend fun getScheduleTimes(teamId: String): MutableList<ScheduleTime>
}