package com.kitchingapp.data.database.repository

import com.kitchingapp.data.database.datasource.FireStoreDataSourceImpl
import com.kitchingapp.data.database.dto.IngredientDTO
import com.kitchingapp.data.database.dto.RecipeDetailDTO
import com.kitchingapp.common.dateFormatter
import com.kitchingapp.common.timeFormatter
import com.kitchingapp.data.database.dto.DepartmentDTO
import com.kitchingapp.data.database.dto.MemberDTO
import com.kitchingapp.data.database.dto.MemberListDTO
import com.kitchingapp.data.database.dto.NoticeDTO
import com.kitchingapp.data.database.dto.OrderCategoryDTO
import com.kitchingapp.data.database.dto.OrderDTO
import com.kitchingapp.data.database.dto.PrepCategoryDTO
import com.kitchingapp.data.database.dto.PrepDTO
import com.kitchingapp.data.database.dto.ScheduleDTO
import com.kitchingapp.data.database.dto.ScheduleTimeListDTO
import com.kitchingapp.data.database.dto.StaffLevelDTO
import com.kitchingapp.data.database.dto.TeamDTO
import com.kitchingapp.data.database.dto.dropDownDepartmentsDTO
import java.time.LocalDate
import java.time.LocalTime

class FireStoreRepositoryImpl(private val dataSource: FireStoreDataSourceImpl): RemoteRepository {

    override suspend fun getTeamsByUserId(userId: String): List<TeamDTO> {
        val teamDTOList = mutableListOf<TeamDTO>()

        dataSource.getTeams(userId).forEach {
            teamDTOList.add(TeamDTO(it.id, it.teamName))
        }

        return teamDTOList
    }

    override suspend fun getDepartmentsForDropDown(teamId: String): List<dropDownDepartmentsDTO> {
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

    /** Recipe Page */
    override suspend fun getRecipeList(teamId: String): MutableList<RecipeDetailDTO> {
        val recipeDTOList = mutableListOf<RecipeDetailDTO>()
        dataSource.getRecipeList(teamId).forEach {
            val ingredientDTOList = mutableListOf<IngredientDTO>()
            it.ingredients.forEach { ingredient ->
                val ingredientDTO = IngredientDTO(
                    ingredientId = ingredient.id,
                    ingredientName = ingredient.name,
                    once = ingredient.once,
                    twice = ingredient.twice,
                    each = ingredient.each
                )
                ingredientDTOList.add(ingredientDTO)
            }
            val recipeDTO = RecipeDetailDTO(
                recipeId = it.id,
                recipeName = it.name,
                picture = it.picture,
                ingredients = ingredientDTOList,
                steps = it.steps
            )
            recipeDTOList.add(recipeDTO)
        }
        return recipeDTOList

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
                prepName = it.name,
                recipeId = it.recipeId,
                recipeName = it.recipeId?.let { id -> dataSource.getRecipeName(id) }
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
                departmentName = if(it.departmentId !== null) dataSource.getDepartmentName(teamId, it.departmentId) else null,
                staffLevelName = if(it.staffLevelId !== null) dataSource.getStaffLevelName(it.staffLevelId) else null
            )
            memberList.add(memberDTO)
        }

        return MemberListDTO(dataSource.getTeamName(teamId), memberList.toList())
    }

    override suspend fun getScheduleTimeList(teamId: String): MutableList<ScheduleTimeListDTO> {
        val scheduleTimeList = mutableListOf<ScheduleTimeListDTO>()

        dataSource.getScheduleTimes(teamId).forEach {
            val scheduleTimeDTO = ScheduleTimeListDTO(
                scheduleTimeId = it.id,
                scheduleTimeName = it.name,
                color = it.color,
                startTime = LocalTime.parse(it.startTime, timeFormatter),
                endTime = LocalTime.parse(it.endTime, timeFormatter)
            )
            scheduleTimeList.add(scheduleTimeDTO)
        }

        return scheduleTimeList
    }

    override suspend fun getDepartments(teamId: String): MutableList<DepartmentDTO> {
        val departmentList = mutableListOf<DepartmentDTO>()

        dataSource.getDepartments(teamId).forEach {
            val departmentDTO = DepartmentDTO(
                departmentId = it.id,
                departmentName = it.name,
                color = it.color
            )
            departmentList.add(departmentDTO)
        }

        return departmentList
    }

    override suspend fun getStaffLevels(departmentId: String): MutableList<StaffLevelDTO> {
        val staffLevelList = mutableListOf<StaffLevelDTO>()

        dataSource.getStaffLevels(departmentId).forEach {
            val staffLevelDTO = StaffLevelDTO(
                staffLevelId = it.id,
                staffLevelName = it.name
            )
            staffLevelList.add(staffLevelDTO)
        }

        return staffLevelList
    }

    override suspend fun getNotices(teamId: String): MutableList<NoticeDTO> {
        val noticeList = mutableListOf<NoticeDTO>()

        dataSource.getNotices(teamId).forEach {
            val noticeDTO = NoticeDTO(
                noticeId = it.id,
                title = it.title,
                content = it.content,
                date = LocalDate.parse(it.date, dateFormatter),
                writerId = it.writerId,
                writerName = dataSource.getUserName(it.writerId)
            )
            noticeList.add(noticeDTO)
        }

        return noticeList
    }
}