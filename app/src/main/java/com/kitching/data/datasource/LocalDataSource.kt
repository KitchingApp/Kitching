package com.kitching.data.datasource

import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun saveTeamId(teamId: String)
    val teamId: Flow<String?>
}