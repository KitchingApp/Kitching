package com.kitchingapp.domain.entities

import java.time.LocalDate
import java.time.LocalTime

data class RestaurantTeam(
    val teamName: String,
    val owner: User,
    val managers: List<User>,
    val members: List<User>,
    val todoCategories: List<TodoCategory>,
    val todos: List<Todo>,
    val orderCategory: List<OrderCategory>,
    val orders: List<Order>,
    val departments: List<Department>,
    val staffLevels: List<StaffLevel>,
    val scheduleTimes: List<ScheduleTime>,
    val recipes: List<Recipe>,
    val schedules: List<Schedule>,
    val notices: List<Notice>
)

data class ScheduleTime(
    val name: String,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val color: Int
)

data class Schedule(
    val applier: User,
    val scheduleTime: ScheduleTime,
    val date: LocalDate,
    val isFix: Boolean = false
)

data class TodoCategory(val name: String, val color: Int)

data class Todo(val name: String, val category: TodoCategory)

data class OrderCategory(val name: String, val color: Int)

data class Order(val name: String, val category: OrderCategory, val count: Int = 0)

data class Department(val name: String, val color: Int)

data class Ingredient(val name: String, val once: Int, val twice: Int, val each: String)

data class Recipe(val name: String, val ingredients: List<Ingredient>, val steps: List<String>)

data class StaffLevel(val name: String, val department: Department)

data class Notice(val title: String, val content: String, val date: LocalDate, val writer: User)