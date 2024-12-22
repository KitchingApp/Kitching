package com.kitchingapp.data.database.datasource

import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun saveTeamId(teamId: String)
    suspend fun getTeamId(): Flow<String?>
}