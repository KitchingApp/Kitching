package com.kitching.data.repository

import com.kitching.data.dto.ScheduleDTO
import com.kitching.data.dto.DropDownDepartmentsDTO
import com.kitching.data.dto.DropDownMembersDTO
import com.kitching.data.dto.ScheduleTimeChipsDTO
import com.kitching.data.firebase.FireStoreDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.firebase.fetchFirebaseDataFlow
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(private val dataSource: FireStoreDataSource = FireStoreDataSource()) {
    suspend fun getDepartmentsForDropDown(teamId: String): Flow<FirebaseResult<List<DropDownDepartmentsDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getDepartments(teamId) },
            mapper = { DropDownDepartmentsDTO(it.id, it.name) }
        )
    }

    suspend fun getSchedules(teamId: String, date: String): Flow<FirebaseResult<List<ScheduleDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getTeamSchedules(teamId, date) },
            mapper = {
                ScheduleDTO(
                    scheduleId = it.id,
                    date = it.date,
                    departmentName = dataSource.getDepartmentName(teamId, dataSource.getDepartmentId(teamId, it.userId)),
                    userId = it.userId,
                    userName = dataSource.getUserName(it.userId),
                    scheduleTimeName = dataSource.getScheduleTimeName(teamId, it.scheduleTimeId),
                    isFix = it.isFix
                )
            }
        )
    }

    suspend fun getMembers(teamId: String): Flow<FirebaseResult<List<DropDownMembersDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getAllMembers(teamId) },
            mapper = {
                DropDownMembersDTO(
                    userId = it.userId,
                    userName = dataSource.getUserName(it.userId)
                )
            }
        )
    }

    suspend fun getScheduleTimes(teamId: String): Flow<FirebaseResult<List<ScheduleTimeChipsDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getScheduleTimes(teamId) },
            mapper = {
                ScheduleTimeChipsDTO(
                    scheduleTimeId = it.id,
                    scheduleTimeName = it.name
                )
            }
        )
    }

    suspend fun createSchedule(teamId: String, dateString: String, userId: String, scheduleTimeId: String, isFix: Boolean = true): Boolean {
        return dataSource.createSchedule(teamId, dateString, userId, scheduleTimeId, isFix)
    }

    suspend fun deleteSchedule(scheduleId: String): Boolean {
        return dataSource.deleteSchedule(scheduleId)
    }

    suspend fun applySchedule(scheduleId: String): Boolean {
        return dataSource.applySchedule(scheduleId)
    }
}