package com.kitchingapp.data.database.repository

import com.kitchingapp.data.database.dto.ScheduleDTO
import com.kitchingapp.domain.entities.Team
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun saveTeamId(teamId: String)
    val teamId: Flow<String?>
}