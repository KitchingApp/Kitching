package com.kitching.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.kitching.common.COLLECTION_TEAM
import com.kitching.domain.datasource.TeamDataSource
import com.kitching.domain.entities.Team
import kotlinx.coroutines.tasks.await

class TeamDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()): TeamDataSource {
    override suspend fun getTeam(teamId: String): Result<Team> {
        return runCatching {
            db.collection(COLLECTION_TEAM).whereEqualTo("id", teamId).get().await()
                .toObjects(Team::class.java).first()
        }
    }

    override suspend fun createTeam(
        ownerId: String,
        inviteCode: String,
        teamName: String
    ): Result<String> {
        return runCatching {
            db.collection(COLLECTION_TEAM).add(
                Team(
                    id = "",
                    inviteCode = inviteCode,
                    ownerId = ownerId,
                    teamName = teamName
                )
            ).await().apply {
                update("id", id).await()
            }.id
        }
    }
}