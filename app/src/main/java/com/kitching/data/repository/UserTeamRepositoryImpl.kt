package com.kitching.data.repository

import com.kitching.data.datasource.MemberInfoDataSourceImpl
import com.kitching.data.datasource.TeamDataSourceImpl
import com.kitching.data.datasource.UserTeamDataSourceImpl
import com.kitching.data.dto.DropDownMembersDTO
import com.kitching.data.dto.MemberDTO
import com.kitching.data.dto.MemberListDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.domain.datasource.MemberInfoDataSource
import com.kitching.domain.datasource.TeamDataSource
import com.kitching.domain.datasource.UserTeamDataSource
import com.kitching.domain.repository.UserTeamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class UserTeamRepositoryImpl(
    private val teamDataSource: TeamDataSource = TeamDataSourceImpl(),
    private val memberInfoDataSource: MemberInfoDataSource = MemberInfoDataSourceImpl()
): UserTeamRepository {
    override fun getAllMembers(teamId: String): Flow<FirebaseResult<MemberListDTO>> = flow {
        emit(FirebaseResult.Loading)
        val teamName = teamDataSource.getTeam(teamId).getOrThrow().teamName
        val members = memberInfoDataSource.getMemberInfos(teamId).getOrThrow().map {
            MemberDTO(
                userId = it.user.id,
                userName = it.user.userName,
                departmentName = it.department?.name,
                staffLevelName = it.staffLevel?.name
            )
        }
        emit(FirebaseResult.Success(
            MemberListDTO(
                teamName = teamName,
                members = members
            )
        ))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun getAllMembersForSchedule(teamId: String): Flow<FirebaseResult<List<DropDownMembersDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val members = memberInfoDataSource.getMemberInfos(teamId).getOrThrow().map {
            DropDownMembersDTO(
                userId = it.user.id,
                userName = it.user.userName,
            )
        }
        emit(FirebaseResult.Success(members))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }
}