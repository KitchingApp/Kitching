package com.kitchingapp.data.database.repository

import com.kitchingapp.data.database.datasource.FireStoreDataSourceImpl
import com.kitchingapp.data.database.dto.MemberDTO
import com.kitchingapp.data.database.dto.MemberListDTO
import com.kitchingapp.data.database.dto.OrderCategoryDTO
import com.kitchingapp.data.database.dto.OrderDTO
import com.kitchingapp.data.database.dto.PrepCategoryDTO
import com.kitchingapp.data.database.dto.PrepDTO
import com.kitchingapp.data.database.dto.ScheduleDTO
import com.kitchingapp.data.database.dto.TeamDTO
import com.kitchingapp.data.database.dto.dropDownDepartmentsDTO

class FireStoreRepositoryImpl(private val dataSource: FireStoreDataSourceImpl): RemoteRepository {

    override suspend fun getTeamsByUserId(userId: String): List<TeamDTO> {
        val teamDTOList = mutableListOf<TeamDTO>()

        dataSource.getTeams(userId).forEach {
            teamDTOList.add(TeamDTO(it.id, it.teamName))
        }

        return teamDTOList
    }

    override suspend fun getDepartments(teamId: String): List<dropDownDepartmentsDTO> {
        val departmentDTOList = mutableListOf<dropDownDepartmentsDTO>()

        dataSource.getDepartments(teamId).forEach {
            departmentDTOList.add(dropDownDepartmentsDTO(it.id, it.name))
        }

        return departmentDTOList
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
        val orderCategoryDTOList = mutableListOf<OrderCategoryDTO>()

        dataSource.getOrderCategory(teamId).forEach {
            val orderDTO = OrderCategoryDTO(
                categoryId = it.id,
                categoryName = it.name,
                color = it.color
            )
            orderCategoryDTOList.add(orderDTO)
        }
        return orderCategoryDTOList
    }

    override suspend fun getOrderList(categoryId: String): MutableList<OrderDTO> {
        val orderDTOList = mutableListOf<OrderDTO>()
        dataSource.getOrderList(categoryId).forEach {
            val orderDTO = OrderDTO(
                orderId = it.id,
                orderName = it.name
            )
            orderDTOList.add(orderDTO)
        }
        return orderDTOList
    }

    /** Prep */

    override suspend fun getPrepCategory(teamId: String): MutableList<PrepCategoryDTO> {
        val prepCategoryDTOList = mutableListOf<PrepCategoryDTO>()

        dataSource.getPrepCategory(teamId).forEach {
            val prepCategoryDTO = PrepCategoryDTO(
                categoryId = it.id,
                categoryName = it.name,
                color = it.color
            )
            prepCategoryDTOList.add(prepCategoryDTO)
        }
        return prepCategoryDTOList
    }

    override suspend fun getPrepList(categoryId: String): MutableList<PrepDTO> {
        val prepDTOList = mutableListOf<PrepDTO>()

        dataSource.getPrepList(categoryId).forEach {
            val prepDTO = PrepDTO(
                prepId = it.id,
                prepName = it.name
            )
            prepDTOList.add(prepDTO)
        }
        return prepDTOList
    }

    override suspend fun getMemberList(teamId: String): MemberListDTO {
        val memberList = mutableListOf<MemberDTO>()

        dataSource.getAllMembers(teamId).forEach {
            val memberDTO = MemberDTO(
                userId = it.userId,
                userName = dataSource.getUserName(it.userId),
                departmentName = dataSource.getDepartmentName(teamId, it.departmentId),
                staffLevelName = dataSource.getStaffLevelName(it.staffLevelId)
            )
            memberList.add(memberDTO)
        }

        return MemberListDTO(dataSource.getTeamName(teamId), memberList.toList())
    }
}