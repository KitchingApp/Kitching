package com.kitchingapp.data.database.repository

import com.kitchingapp.data.database.datasource.FireStoreDataSourceImpl
import com.kitchingapp.data.database.dto.ScheduleDTO
import com.kitchingapp.data.database.dto.TeamDTO

class FireStoreRepositoryImpl(private val dataSource: FireStoreDataSourceImpl): RemoteRepository {

    override suspend fun getTeamsByUserId(userId: String): List<TeamDTO> {
        val teamDTOList = mutableListOf<TeamDTO>()

        dataSource.getTeams(userId).forEach {
            teamDTOList.add(TeamDTO(it.id, it.teamName))
        }

        return teamDTOList
    }

//    override suspend fun getDepartments(teamId: String): List<dropDownDepartmentsDTO> {
//        val departmentDTOList = mutableListOf<dropDownDepartmentsDTO>()
//
//        dataSource.getDepartments(teamId).forEach {
//            departmentDTOList.add(dropDownDepartmentsDTO(it.id, it.name))
//        }
//
//        return departmentDTOList
//    }

    override suspend fun getSchedules(teamId: String, date: String): List<ScheduleDTO> {
        val scheduleDTOList = mutableListOf<ScheduleDTO>()

        dataSource.getTeamSchedules(teamId, date).forEach {
            val scheduleDTO = ScheduleDTO(
                scheduleId = it.id,
                date = it.date,
                departmentName = dataSource.getDepartmentName(teamId, dataSource.getDepartmentId(teamId, it.userId)),
                userId = it.userId,
                userName = dataSource.getUserName(it.userId),
                scheduleTimeName = dataSource.getScheduleTimeName(teamId, it.scheduleTimeId),
                isFix = it.isFix
            )
            scheduleDTOList.add(scheduleDTO)
        }

        return scheduleDTOList
    }

    override suspend fun deleteSchedule(scheduleId: String): Boolean {
        return dataSource.deleteSchedule(scheduleId)
    }
}