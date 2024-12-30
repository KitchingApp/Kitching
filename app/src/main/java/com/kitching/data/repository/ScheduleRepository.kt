package com.kitching.data.repository

import com.kitching.data.dto.ScheduleDTO
import com.kitching.data.dto.dropDownDepartmentsDTO
import com.kitching.data.firebase.FireStoreDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.firebase.fetchFirebaseDataFlow
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(private val dataSource: FireStoreDataSource = FireStoreDataSource()) {
    suspend fun getDepartmentsForDropDown(teamId: String): Flow<FirebaseResult<List<dropDownDepartmentsDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getDepartments(teamId) },
            mapper = { dropDownDepartmentsDTO(it.id, it.name) }
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

    suspend fun deleteSchedule(scheduleId: String): Boolean {
        return dataSource.deleteSchedule(scheduleId)
    }
}