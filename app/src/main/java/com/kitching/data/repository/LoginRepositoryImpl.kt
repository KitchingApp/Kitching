package com.kitching.data.repository

import com.kitching.data.datasource.TeamUserTeamJoinDataSourceImpl
import com.kitching.data.datasource.UserDataSourceImpl
import com.kitching.data.dto.TeamDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.domain.datasource.TeamUserTeamJoinDataSource
import com.kitching.domain.datasource.UserDataSource
import com.kitching.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class LoginRepositoryImpl(
    private val teamUserTeamJoinDataSource: TeamUserTeamJoinDataSource = TeamUserTeamJoinDataSourceImpl(),
    private val userDataSource: UserDataSource = UserDataSourceImpl()
): LoginRepository {
    override fun getTeamList(userId: String): Flow<FirebaseResult<List<TeamDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val teamList = teamUserTeamJoinDataSource.getTeams(userId)
            if(teamList.isEmpty()) emit(FirebaseResult.Success(emptyList()))
        else emit(FirebaseResult.Success(teamList.map {
                TeamDTO(
                    teamId = it.id,
                    teamName = it.teamName
                )
            }))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun checkAndSaveUser(
        uid: String,
        userName: String,
        userImage: String
    ): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = userDataSource.checkAndSaveUser(uid, userName, userImage)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }
}