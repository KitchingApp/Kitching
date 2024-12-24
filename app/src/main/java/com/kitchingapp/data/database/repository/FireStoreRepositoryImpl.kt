package com.kitchingapp.data.database.repository

import com.kitchingapp.data.database.datasource.FireStoreDataSourceImpl
import com.kitchingapp.data.database.dto.OrderCategoryDTO
import com.kitchingapp.data.database.dto.ScheduleDTO
import com.kitchingapp.domain.entities.Team

class FireStoreRepositoryImpl(private val dataSource: FireStoreDataSourceImpl): RemoteRepository {

    override suspend fun getTeamsByUserId(userId: String): List<Team> {
        return dataSource.getTeams(userId)
    }

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

    /** Order Page */
    override suspend fun getOrderCategory(teamId: String): MutableList<OrderCategoryDTO> {
        val orderDTOList = mutableListOf<OrderCategoryDTO>()

        dataSource.getOrderCategory(teamId).forEach {
            val orderDTO = OrderCategoryDTO(
                categoryId = it.id,
                categoryName = it.name,
                color = it.color
            )
            orderDTOList.add(orderDTO)
        }
        return orderDTOList
    }
}