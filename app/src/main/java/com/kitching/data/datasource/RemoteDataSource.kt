package com.kitching.data.datasource

import com.kitching.domain.entities.Order
import com.kitching.domain.entities.OrderCategory
import com.kitching.domain.entities.Department
import com.kitching.domain.entities.Recipe
import com.kitching.domain.entities.Notice
import com.kitching.domain.entities.Prep
import com.kitching.domain.entities.PrepCategory
import com.kitching.domain.entities.Schedule
import com.kitching.domain.entities.ScheduleTime
import com.kitching.domain.entities.StaffLevel
import com.kitching.domain.entities.Team
import com.kitching.domain.entities.UserTeam

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

    /** Recipe Page */

    suspend fun getRecipeList(teamId: String): MutableList<Recipe>

    /** Prep */

    suspend fun getPrepCategory(teamId: String): MutableList<PrepCategory>

    suspend fun getPrepList(categoryId: String): MutableList<Prep>

    /** other */

    suspend fun getAllMembers(teamId: String): MutableList<UserTeam>

    suspend fun getRecipeName(recipeId: String): String

    suspend fun getScheduleTimes(teamId: String): MutableList<ScheduleTime>

    suspend fun getStaffLevels(departmentId: String): MutableList<StaffLevel>

    suspend fun getNotices(teamId: String): MutableList<Notice>
}