package com.kitchingapp.domain.entities

data class Team(val id: String, val inviteCode: String, val ownerId: String, val teamName: String)

data class Schedule(
    val id: String,
    val date: String,
    val scheduleTimeId: String,
    val teamId: String,
    val userId: String,
    val isFix: Boolean
)

data class ScheduleTime(
    val id: String,
    val name: String,
    val color: String,
    val startTime: String,
    val endTime: String,
    val teamId: String
)

data class OrderCategory(val color: String, val id: String, val name: String, val teamId: String)

data class Order(val categoryId: String, val id: String, val name: String)

data class PrepCategory(val color: String, val id: String, val name: String, val teamId: String)

data class Prep(val categoryId: String, val id: String, val name: String, val recipeId: String)

data class Department(val color: String, val id: String, val name: String, val teamId: String)

data class StaffLevel(val id: String, val name: String, val departmentId: String)

data class Recipe(
    val id: String,
    val name: String,
    val picture: String,
    val ingredients: List<Ingredient>,
    val steps: List<String>,
    val teamId: String
)

data class Ingredient(
    val id: String,
    val name: String,
    val once: Int,
    val twice: Int,
    val unit: String
)