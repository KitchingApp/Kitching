package com.kitchingapp.data.database.repository

import com.kitchingapp.data.database.datasource.FireStoreDataSourceImpl
import com.kitchingapp.data.database.datasource.PreferencesDataSourceImpl
import com.kitchingapp.data.database.dto.ScheduleDTO
import com.kitchingapp.domain.entities.Team
import kotlinx.coroutines.flow.Flow

class PreferencesRepositoryImpl(private val dataSource: PreferencesDataSourceImpl): LocalRepository {
    override suspend fun saveTeamId(teamId: String) {
        dataSource.saveTeamId(teamId)
    }

    override val teamId = dataSource.teamId

}