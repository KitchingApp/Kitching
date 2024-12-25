package com.kitchingapp.data.database.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.kitchingapp.domain.entities.Order
import com.kitchingapp.domain.entities.OrderCategory
import com.kitchingapp.domain.entities.Department
import com.kitchingapp.domain.entities.Schedule
import com.kitchingapp.domain.entities.Team
import kotlinx.coroutines.tasks.await

class FireStoreDataSourceImpl(private val db: FirebaseFirestore): RemoteDataSource {

    override suspend fun getTeams(userId: String): List<Team> {
        val teams = db.collection("team").whereEqualTo("ownerId", userId).get().await()

        return if(teams.isEmpty) emptyList()
        else teams.toObjects(Team::class.java)
    }

    override suspend fun getDepartments(teamId: String): List<Department> {
        val departments = db.collection("department").whereEqualTo("teamId", teamId).get().await()

        return if(departments.isEmpty) emptyList()
        else departments.toObjects(Department::class.java)
    }

    override suspend fun getTeamSchedules(teamId: String, date: String): List<Schedule> {
        val schedules = db.collection("schedule").whereEqualTo("teamId", teamId).whereEqualTo("date", date).get().await()

        return if(schedules.isEmpty) emptyList()
        else schedules.toObjects(Schedule::class.java)
    }

    override suspend fun getScheduleTimeName(teamId: String, scheduleTimeId: String): String {
        val scheduleTime = db.collection("scheduleTime").whereEqualTo("id", scheduleTimeId).get().await().documents.firstOrNull()

        return scheduleTime?.getString("name")!!
    }

    override suspend fun getDepartmentId(teamId: String, userId: String): String {
        val userTeam = db.collection("user-team").whereEqualTo("teamId", teamId).whereEqualTo("userId", userId).get().await().documents.firstOrNull()

        return userTeam?.getString("departmentId")!!
    }

    override suspend fun getDepartmentName(teamId: String, departmentId: String): String {
        val department = db.collection("department").whereEqualTo("id", departmentId).get().await().documents.firstOrNull()

        return department?.getString("name")!!
    }

    override suspend fun getUserName(userId: String): String {
        val user = db.collection("user").whereEqualTo("id", userId).get().await().documents.firstOrNull()

        return user?.getString("userName")!!
    }

    override suspend fun createSchedule(schedule: Schedule): Boolean {
        val scheduleMap = mapOf(
            "id" to schedule.id,
            "date" to schedule.date,
            "scheduleTimeId" to schedule.scheduleTimeId,
            "teamId" to schedule.teamId,
            "userId" to schedule.userId,
            "isFix" to schedule.isFix
        )
        val createTask = db.collection("schedule").add(scheduleMap).await().get()
        return createTask.exception == null
    }

    override suspend fun deleteSchedule(scheduleId: String): Boolean {
        var deleteTaskResult = false

        db.collection("schedule").document(scheduleId).delete()
            .addOnSuccessListener { deleteTaskResult = true }

        return deleteTaskResult
    }

    /** Order Page */

    override suspend fun getOrderCategory(teamId: String): MutableList<OrderCategory> {
        val orderCategory = db.collection("orderCategory").whereEqualTo("teamId", teamId).get().await()
        return if (orderCategory.isEmpty) mutableListOf()
        else orderCategory.toObjects(OrderCategory::class.java) as MutableList<OrderCategory>
    }

    override suspend fun getOrderList(categoryId: String): MutableList<Order> {
        val orderList = db.collection("order").whereEqualTo("categoryId", categoryId).get().await()
        return if (orderList.isEmpty) mutableListOf()
        else orderList.toObjects(Order::class.java) as MutableList<Order>

    }
}