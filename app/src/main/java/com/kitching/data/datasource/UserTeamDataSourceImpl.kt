package com.kitching.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.kitching.common.COLLECTION_USER_TEAM
import com.kitching.domain.datasource.UserTeamDataSource
import com.kitching.domain.entities.UserTeam
import kotlinx.coroutines.tasks.await

class UserTeamDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) :
    UserTeamDataSource {
    override suspend fun getAllMembers(teamId: String): List<UserTeam> {
        return db.collection(COLLECTION_USER_TEAM).whereEqualTo("teamId", teamId).get().await()
            .toObjects(UserTeam::class.java)
    }

    override suspend fun getMember(teamId: String, userId: String): UserTeam? {
        val userTeam = db.collection(COLLECTION_USER_TEAM).whereEqualTo("teamId", teamId)
            .whereEqualTo("userId", userId).get().await()
        return if(userTeam.isEmpty) null
        else userTeam.documents.first().toObject(UserTeam::class.java)
    }

    override suspend fun getUserTeams(userId: String): List<UserTeam> {
        return db.collection(COLLECTION_USER_TEAM).whereEqualTo("userId", userId).get().await()
            .toObjects(UserTeam::class.java)
    }

    override suspend fun createUserTeams(
        userId: String,
        teamId: String,
        isManager: Boolean
    ): Boolean {
        return runCatching {
            db.collection(COLLECTION_USER_TEAM).add(
                UserTeam(
                    id = "",
                    userId = userId,
                    teamId = teamId,
                    isManager = isManager
                )
            ).await().apply {
                this.update("id", id).await()
            }
        }.isSuccess
    }

    override suspend fun updateMemberManagerState(
        teamId: String,
        userId: String,
        isManager: Boolean
    ): Boolean {
        return runCatching {
            db.collection(COLLECTION_USER_TEAM).whereEqualTo("teamId", teamId)
                .whereEqualTo("userId", userId).get()
                .await().documents.first().reference.update("isManager", isManager).await()
        }.isSuccess
    }
}