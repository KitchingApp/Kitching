package com.kitching.domain.repository

import com.kitching.data.dto.TeamDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface TeamRepository {
    fun getTeamsByUserId(userId: String): Flow<FirebaseResult<List<TeamDTO>>>

    fun getTeam(teamId: String): Flow<FirebaseResult<TeamDTO>>

    fun createTeam(ownerId: String, teamName: String): Flow<FirebaseResult<Boolean>>
}