package com.kitching.domain.datasource

import com.kitching.domain.entities.Team

interface TeamDataSource {
    suspend fun getTeam(teamId: String): Result<Team>

    suspend fun createTeam(ownerId: String, inviteCode: String, teamName: String): Result<String>
}