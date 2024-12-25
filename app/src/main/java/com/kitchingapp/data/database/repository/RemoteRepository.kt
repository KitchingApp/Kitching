package com.kitchingapp.data.database.repository

import com.kitchingapp.data.database.dto.OrderCategoryDTO
import com.kitchingapp.data.database.dto.OrderDTO
import com.kitchingapp.data.database.dto.ScheduleDTO
import com.kitchingapp.domain.entities.Team

interface RemoteRepository {

    suspend fun getTeamsByUserId(userId: String): List<Team>

    suspend fun getSchedules(teamId: String, date: String): List<ScheduleDTO>

    suspend fun deleteSchedule(scheduleId: String): Boolean

    /** Oder Page */

    suspend fun getOrderCategory(teamId: String): MutableList<OrderCategoryDTO>

    suspend fun getOrderList(categoryId: String): MutableList<OrderDTO>
}