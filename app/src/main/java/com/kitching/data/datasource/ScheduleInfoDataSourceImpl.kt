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
) : ScheduleInfoDataSource {
    override suspend fun getScheduleInfos(teamId: String, date: String): List<ScheduleInfo> {
        return scheduleDataSource.getSchedules(teamId, date).map {
            ScheduleInfo(
                schedule = it,
                user = userDataSource.getUser(it.userId) ?: throw Throwable("user is not exists"),
                department = userTeamDataSource.getMember(it.teamId, it.userId)?.departmentId?.let { departmentId ->
                    departmentDataSource.getDepartment(
                        departmentId
                    )
                },
                scheduleTime = scheduleTimeDataSource.getScheduleTime(it.scheduleTimeId) ?: throw Throwable("scheduleTime is not exists")
            )
        }
    }
}