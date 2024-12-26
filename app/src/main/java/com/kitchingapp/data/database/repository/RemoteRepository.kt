package com.kitchingapp.data.database.repository

import com.kitchingapp.data.database.dto.DepartmentDTO
import com.kitchingapp.data.database.dto.MemberListDTO
import com.kitchingapp.data.database.dto.OrderCategoryDTO
import com.kitchingapp.data.database.dto.OrderDTO
import com.kitchingapp.data.database.dto.RecipeDetailDTO
import com.kitchingapp.data.database.dto.PrepCategoryDTO
import com.kitchingapp.data.database.dto.PrepDTO
import com.kitchingapp.data.database.dto.ScheduleDTO
import com.kitchingapp.data.database.dto.ScheduleTimeListDTO
import com.kitchingapp.data.database.dto.StaffLevelDTO
import com.kitchingapp.data.database.dto.dropDownDepartmentsDTO
import com.kitchingapp.data.database.dto.TeamDTO

interface RemoteRepository {

    suspend fun getTeamsByUserId(userId: String): List<TeamDTO>

    suspend fun getDepartmentsForDropDown(teamId: String): List<dropDownDepartmentsDTO>

    suspend fun getSchedules(teamId: String, date: String): List<ScheduleDTO>

    suspend fun deleteSchedule(scheduleId: String): Boolean

    /** Oder Page */

    suspend fun getOrderCategory(teamId: String): MutableList<OrderCategoryDTO>

    suspend fun getOrderList(categoryId: String): MutableList<OrderDTO>

    /** Recipe Page */

    suspend fun getRecipeList(teamId: String): MutableList<RecipeDetailDTO>

    /** Prep */

    suspend fun getPrepCategory(teamId: String): MutableList<PrepCategoryDTO>

    suspend fun getPrepList(categoryId: String): MutableList<PrepDTO>

    /** Other */

    suspend fun getMemberList(teamId: String): MemberListDTO

    suspend fun getScheduleTimeList(teamId: String): MutableList<ScheduleTimeListDTO>

    suspend fun getDepartments(teamId: String): MutableList<DepartmentDTO>

    suspend fun getStaffLevels(departmentId: String): MutableList<StaffLevelDTO>

}