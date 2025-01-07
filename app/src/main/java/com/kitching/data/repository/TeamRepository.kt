package com.kitching.data.repository

import com.kitching.data.dto.TeamDTO
import com.kitching.data.firebase.FireStoreDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.firebase.fetchFirebaseBooleanFlow
import com.kitching.data.firebase.fetchFirebaseDataFlow
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class TeamRepository(private val dataSource: FireStoreDataSource = FireStoreDataSource()) {
    suspend fun getTeamsByUserId(userId: String): Flow<FirebaseResult<List<TeamDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getTeams(userId) },
            mapper = { TeamDTO(it.id, it.teamName) }
        )
    }

    suspend fun createTeam(
        ownerId: String, teamName: String
    ): Flow<FirebaseResult<Unit>> {
        val inviteCode = UUID.randomUUID().toString().replace("-", "")

        return fetchFirebaseBooleanFlow {
            dataSource.createTeam(inviteCode, ownerId, teamName)
        }
    }
}