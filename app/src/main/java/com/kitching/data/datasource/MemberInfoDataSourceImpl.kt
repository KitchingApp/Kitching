package com.kitching.data.datasource

import com.kitching.domain.datasource.DepartmentDataSource
import com.kitching.domain.datasource.MemberInfoDataSource
import com.kitching.domain.datasource.StaffLevelDataSource
import com.kitching.domain.datasource.TeamDataSource
import com.kitching.domain.datasource.UserDataSource
import com.kitching.domain.datasource.UserTeamDataSource
import com.kitching.domain.entities.MemberInfo

class MemberInfoDataSourceImpl(
    private val userDataSource: UserDataSource = UserDataSourceImpl(),
    private val userTeamDataSource: UserTeamDataSource = UserTeamDataSourceImpl(),
    private val departmentDataSource: DepartmentDataSource = DepartmentDataSourceImpl(),
    private val staffLevelDataSource: StaffLevelDataSource = StaffLevelDataSourceImpl()
) : MemberInfoDataSource {
    override suspend fun getMemberInfos(teamId: String): List<MemberInfo> {
        return userTeamDataSource.getAllMembers(teamId).map {
            MemberInfo(
                userTeam = it,
                user = userDataSource.getUser(it.userId) ?: throw Throwable("user is not exists"),
                department = it.departmentId?.let { departmentId ->
                    departmentDataSource.getDepartment(departmentId)
                },
                staffLevel = it.staffLevelId?.let { staffLevelId ->
                    staffLevelDataSource.getStaffLevel(staffLevelId)
                }
            )
        }
    }
}