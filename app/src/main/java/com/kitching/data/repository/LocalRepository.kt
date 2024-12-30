package com.kitching.data.repository

import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun saveTeamId(teamId: String)
    val teamId: Flow<String?>
}