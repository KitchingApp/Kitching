package com.kitching.domain.datasource

import com.kitching.domain.entities.UserTeam

interface UserTeamDataSource {
    suspend fun getAllMembers(teamId: String): List<UserTeam>

    suspend fun getMember(teamId: String, userId: String): UserTeam?

    suspend fun getUserTeams(userId: String): List<UserTeam>

    suspend fun createUserTeams(userId: String, teamId: String, isManager: Boolean): Boolean

    suspend fun updateMemberManagerState(teamId: String, userId: String, isManager: Boolean): Boolean
}