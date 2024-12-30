package com.kitching.data.repository

import com.kitching.common.dateFormatter
import com.kitching.common.timeFormatter
import com.kitching.data.dto.DepartmentDTO
import com.kitching.data.dto.MemberDTO
import com.kitching.data.dto.MemberListDTO
import com.kitching.data.dto.NoticeDTO
import com.kitching.data.dto.ScheduleTimeListDTO
import com.kitching.data.dto.StaffLevelDTO
import com.kitching.data.firebase.FireStoreDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.firebase.fetchFirebaseDataFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.LocalTime

class OtherRepository(private val dataSource: FireStoreDataSource = FireStoreDataSource()) {
    suspend fun getMemberList(teamId: String): Flow<FirebaseResult<MemberListDTO>> = flow {
        emit(FirebaseResult.Loading)
        runCatching {
            dataSource.getAllMembers(teamId).map {
                MemberDTO(
                    userId = it.userId,
                    userName = dataSource.getUserName(it.userId),
                    departmentName = if (it.departmentId !== null) dataSource.getDepartmentName(
                        teamId,
                        it.departmentId
                    ) else null,
                    staffLevelName = if (it.staffLevelId !== null) dataSource.getStaffLevelName(it.staffLevelId) else null
                )
            }
        }
            .onSuccess {
                emit(
                    FirebaseResult.Success(
                    MemberListDTO(
                        dataSource.getTeamName(teamId),
                        it.toList()
                    )
                ))
            }
            .onFailure { emit(FirebaseResult.Failure(it)) }
    }

    suspend fun getScheduleTimeList(teamId: String): Flow<FirebaseResult<MutableList<ScheduleTimeListDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getScheduleTimes(teamId) },
            mapper = {
                ScheduleTimeListDTO(
                    scheduleTimeId = it.id,
                    scheduleTimeName = it.name,
                    color = it.color,
                    startTime = LocalTime.parse(it.startTime, timeFormatter),
                    endTime = LocalTime.parse(it.endTime, timeFormatter)
                )
            }
        )
    }

    suspend fun getDepartments(teamId: String): Flow<FirebaseResult<MutableList<DepartmentDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getDepartments(teamId) },
            mapper = {
                DepartmentDTO(
                    departmentId = it.id,
                    departmentName = it.name,
                    color = it.color
                )
            }
        )
    }

    suspend fun getStaffLevels(departmentId: String): Flow<FirebaseResult<MutableList<StaffLevelDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getStaffLevels(departmentId) },
            mapper = {
                StaffLevelDTO(
                    staffLevelId = it.id,
                    staffLevelName = it.name
                )
            }
        )
    }

    suspend fun getNotices(teamId: String): Flow<FirebaseResult<MutableList<NoticeDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = {dataSource.getNotices(teamId)},
            mapper = {
                NoticeDTO(
                    noticeId = it.id,
                    title = it.title,
                    content = it.content,
                    date = LocalDate.parse(it.date, dateFormatter),
                    writerId = it.writerId,
                    writerName = dataSource.getUserName(it.writerId)
                )
            }
        )
    }
}