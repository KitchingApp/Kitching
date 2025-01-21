package com.kitching.data.repository

import com.kitching.data.datasource.TeamDataSourceImpl
import com.kitching.data.datasource.TeamUserTeamJoinDataSourceImpl
import com.kitching.data.datasource.UserTeamDataSourceImpl
import com.kitching.data.dto.TeamDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.firebase.fetchFirebaseDataFlow
import com.kitching.domain.datasource.TeamDataSource
import com.kitching.domain.datasource.TeamUserTeamJoinDataSource
import com.kitching.domain.datasource.UserTeamDataSource
import com.kitching.domain.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.util.UUID

class TeamRepositoryImpl(
    private val teamDataSource: TeamDataSource = TeamDataSourceImpl(),
    private val userTeamDataSource: UserTeamDataSource = UserTeamDataSourceImpl(),
    private val teamUserTeamJoinDataSource: TeamUserTeamJoinDataSource = TeamUserTeamJoinDataSourceImpl()
) : TeamRepository {
    override fun getTeamsByUserId(userId: String): Flow<FirebaseResult<List<TeamDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val teams = teamUserTeamJoinDataSource.getTeams(userId).getOrThrow().map {
            TeamDTO(
                teamId = it.id,
                teamName = it.teamName
            )
        }
        emit(FirebaseResult.Success(teams))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun getTeam(teamId: String): Flow<FirebaseResult<TeamDTO>> = flow {
        emit(FirebaseResult.Loading)
        val team = teamDataSource.getTeam(teamId).getOrThrow()
        emit(
            FirebaseResult.Success(
                TeamDTO(
                    teamId = team.id,
                    teamName = team.teamName
                )
            )
        )
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun createTeam(
        ownerId: String, teamName: String
    ): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val inviteCode = UUID.randomUUID().toString().replace("-", "")
        val teamId = teamDataSource.createTeam(inviteCode, ownerId, teamName).getOrThrow()
        val result = userTeamDataSource.createUserTeams(userId = ownerId, teamId = teamId, true)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }
}