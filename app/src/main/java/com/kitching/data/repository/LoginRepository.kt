package com.kitching.data.repository

import com.kitching.data.dto.TeamDTO
import com.kitching.data.firebase.FireStoreDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.firebase.fetchFirebaseBooleanFlow
import com.kitching.data.firebase.fetchFirebaseDataFlow
import kotlinx.coroutines.flow.Flow

class LoginRepository(private val dataSource: FireStoreDataSource = FireStoreDataSource()) {
    suspend fun getTeamList(userId: String): Flow<FirebaseResult<MutableList<TeamDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getTeams(userId) },
            mapper = { TeamDTO(
                teamId = it.id,
                teamName = it.teamName
            ) }
        )
    }

    suspend fun checkAndSaveUser(uid: String, userName: String, userImage: String): Flow<FirebaseResult<Unit>> {
        return fetchFirebaseBooleanFlow {
            dataSource.checkAndSaveUser(uid, userName, userImage)
        }
    }
}