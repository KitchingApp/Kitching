package com.kitching.domain.repository

import com.kitching.data.dto.TeamDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun getTeamList(userId: String): Flow<FirebaseResult<List<TeamDTO>>>

    fun checkAndSaveUser(uid: String, userName: String, userImage: String): Flow<FirebaseResult<Boolean>>
}