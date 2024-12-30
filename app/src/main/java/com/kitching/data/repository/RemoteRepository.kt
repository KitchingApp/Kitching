package com.kitching.data.repository

import com.kitching.data.dto.DepartmentDTO
import com.kitching.data.dto.MemberListDTO
import com.kitching.data.dto.NoticeDTO
import com.kitching.data.dto.OrderCategoryDTO
import com.kitching.data.dto.OrderDTO
import com.kitching.data.dto.RecipeDetailDTO
import com.kitching.data.dto.PrepCategoryDTO
import com.kitching.data.dto.PrepDTO
import com.kitching.data.dto.ScheduleDTO
import com.kitching.data.dto.ScheduleTimeListDTO
import com.kitching.data.dto.StaffLevelDTO
import com.kitching.data.dto.dropDownDepartmentsDTO
import com.kitching.data.dto.TeamDTO

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

    suspend fun getNotices(teamId: String): MutableList<NoticeDTO>
}