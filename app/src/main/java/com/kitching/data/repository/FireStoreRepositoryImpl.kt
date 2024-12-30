package com.kitching.data.repository

import com.kitching.data.firebase.FireStoreDataSourceImpl
import com.kitching.data.dto.IngredientDTO
import com.kitching.data.dto.RecipeDetailDTO
import com.kitching.common.dateFormatter
import com.kitching.common.timeFormatter
import com.kitching.data.dto.DepartmentDTO
import com.kitching.data.dto.MemberDTO
import com.kitching.data.dto.MemberListDTO
import com.kitching.data.dto.NoticeDTO
import com.kitching.data.dto.OrderCategoryDTO
import com.kitching.data.dto.OrderDTO
import com.kitching.data.dto.PrepCategoryDTO
import com.kitching.data.dto.PrepDTO
import com.kitching.data.dto.ScheduleDTO
import com.kitching.data.dto.ScheduleTimeListDTO
import com.kitching.data.dto.StaffLevelDTO
import com.kitching.data.dto.TeamDTO
import com.kitching.data.dto.dropDownDepartmentsDTO
import com.kitching.data.repository.RemoteRepository
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