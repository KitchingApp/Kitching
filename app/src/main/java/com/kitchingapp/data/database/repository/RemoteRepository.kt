package com.kitchingapp.data.database.repository

import com.kitchingapp.data.database.dto.ScheduleDTO
import com.kitchingapp.domain.entities.Team

interface RemoteRepository {

    suspend fun getTeamsByUserId(userId: String): List<Team>

    suspend fun getScheduleDTO(teamId: String, date: String): List<ScheduleDTO>
}