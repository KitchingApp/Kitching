package com.kitchingapp.data.database.repository

import android.util.Log
import com.kitchingapp.data.database.datasource.FireStoreDataSourceImpl
import com.kitchingapp.data.database.dto.IngredientDTO
import com.kitchingapp.data.database.dto.OrderCategoryDTO
import com.kitchingapp.data.database.dto.OrderDTO
import com.kitchingapp.data.database.dto.RecipeDetailDTO
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
}