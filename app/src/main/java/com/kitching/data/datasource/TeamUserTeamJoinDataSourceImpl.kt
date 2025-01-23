package com.kitching.data.datasource

import com.kitching.domain.datasource.TeamDataSource
import com.kitching.domain.datasource.TeamUserTeamJoinDataSource
import com.kitching.domain.datasource.UserTeamDataSource
import com.kitching.domain.entities.Team

class TeamUserTeamJoinDataSourceImpl(
    private val teamDataSource: TeamDataSource = TeamDataSourceImpl(),
    private val userTeamDataSource: UserTeamDataSource = UserTeamDataSourceImpl()
) : TeamUserTeamJoinDataSource {
    override suspend fun getTeams(userId: String): List<Team> {
        return userTeamDataSource.getUserTeams(userId).map {
            teamDataSource.getTeam(it.teamId) ?: throw Throwable("team is not exists")
        }
    }
}