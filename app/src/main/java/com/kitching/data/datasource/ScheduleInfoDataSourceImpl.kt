package com.kitching.data.datasource

import com.kitching.domain.datasource.DepartmentDataSource
import com.kitching.domain.datasource.ScheduleDataSource
import com.kitching.domain.datasource.ScheduleInfoDataSource
import com.kitching.domain.datasource.ScheduleTimeDataSource
import com.kitching.domain.datasource.UserDataSource
import com.kitching.domain.datasource.UserTeamDataSource
import com.kitching.domain.entities.ScheduleInfo

class ScheduleInfoDataSourceImpl(
    private val scheduleDataSource: ScheduleDataSource = ScheduleDataSourceImpl(),
    private val userTeamDataSource: UserTeamDataSource = UserTeamDataSourceImpl(),
    private val userDataSource: UserDataSource = UserDataSourceImpl(),
    private val departmentDataSource: DepartmentDataSource = DepartmentDataSourceImpl(),
    private val scheduleTimeDataSource: ScheduleTimeDataSource = ScheduleTimeDataSourceImpl()
): ScheduleInfoDataSource {
    override suspend fun getScheduleInfos(teamId: String, date: String): Result<List<ScheduleInfo>> {
        return runCatching {
            scheduleDataSource.getSchedules(teamId, date).getOrThrow().map {
                ScheduleInfo(
                    schedule = it,
                    user = userDataSource.getUser(it.userId).getOrThrow(),
                    department = userTeamDataSource.getMember(it.teamId, it.userId).getOrThrow().departmentId?.let { departmentId ->
                        departmentDataSource.getDepartment(
                            departmentId
                        ).getOrNull()
                    },
                    scheduleTime = scheduleTimeDataSource.getScheduleTime(it.scheduleTimeId).getOrThrow()
                )
            }
        }
    }
}