package com.kitching.data.repository

import com.kitching.data.dto.TeamDTO
import com.kitching.data.firebase.FireStoreDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.firebase.fetchFirebaseDataFlow
import kotlinx.coroutines.flow.Flow

class TeamRepository(private val dataSource: FireStoreDataSource = FireStoreDataSource()) {
    suspend fun getTeamsByUserId(userId: String): Flow<FirebaseResult<List<TeamDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getTeams(userId) },
            mapper = { TeamDTO(it.id, it.teamName) }
        )
    }
}