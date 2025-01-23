package com.kitching.domain.datasource

import com.kitching.domain.entities.Team

interface TeamUserTeamJoinDataSource {
    suspend fun getTeams(userId: String): List<Team>
}