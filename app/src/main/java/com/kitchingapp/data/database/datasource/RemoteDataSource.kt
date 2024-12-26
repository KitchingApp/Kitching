package com.kitchingapp.data.database.datasource

import com.kitchingapp.domain.entities.Order
import com.kitchingapp.domain.entities.OrderCategory
import com.kitchingapp.domain.entities.Department
import com.kitchingapp.domain.entities.Recipe
import com.kitchingapp.domain.entities.Schedule
import com.kitchingapp.domain.entities.Team

interface RemoteDataSource {
    suspend fun getTeams(userId: String): List<Team>

    suspend fun getDepartments(teamId: String): List<Department>

    suspend fun getTeamSchedules(teamId: String, date: String): List<Schedule>

    suspend fun getScheduleTimeName(teamId: String, scheduleTimeId: String): String

    suspend fun getDepartmentId(teamId: String, userId: String): String

    suspend fun getDepartmentName(teamId: String, departmentId: String): String

    suspend fun getUserName(userId: String): String

    suspend fun createSchedule(schedule: Schedule): Boolean

    suspend fun deleteSchedule(scheduleId: String): Boolean

    /** Order Page */

    suspend fun getOrderCategory(teamId: String): MutableList<OrderCategory>

    suspend fun getOrderList(categoryId: String): MutableList<Order>

    /** Recipe Page */

    suspend fun getRecipeList(teamId: String): MutableList<Recipe>
}