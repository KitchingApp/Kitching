package com.kitching.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.kitching.data.datasource.RemoteDataSource
import com.kitching.domain.entities.Order
import com.kitching.domain.entities.OrderCategory
import com.kitching.domain.entities.Department
import com.kitching.domain.entities.Ingredient
import com.kitching.domain.entities.Recipe
import com.kitching.domain.entities.Notice
import com.kitching.domain.entities.Prep
import com.kitching.domain.entities.PrepCategory
import com.kitching.domain.entities.Schedule
import com.kitching.domain.entities.ScheduleTime
import com.kitching.domain.entities.StaffLevel
import com.kitching.domain.entities.Team
import com.kitching.domain.entities.UserTeam
import kotlinx.coroutines.tasks.await

class FireStoreDataSourceImpl(private val db: FirebaseFirestore): RemoteDataSource {

    override suspend fun getTeams(userId: String): List<Team> {
        val teams = db.collection("team").whereEqualTo("ownerId", userId).get().await()

        return if(teams.isEmpty) emptyList()
        else teams.toObjects(Team::class.java)
    }

    override suspend fun getTeamName(teamId: String): String {
        val teamName = db.collection("team").whereEqualTo("id", teamId).get().await().documents.firstOrNull()

        return teamName?.getString("teamName")!!
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

    override suspend fun getStaffLevelId(teamId: String, userId: String): String {
        val userTeam = db.collection("user-team").whereEqualTo("teamId", teamId).whereEqualTo("userId", userId).get().await().documents.firstOrNull()

        return userTeam?.getString("staffLevelId")!!
    }

    override suspend fun getStaffLevelName(staffLevelId: String): String {
        val department = db.collection("staffLevel").whereEqualTo("id", staffLevelId).get().await().documents.firstOrNull()

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

    /** Recipe Page */
    override suspend fun getRecipeList(teamId: String): MutableList<Recipe> {
        val recipeListSnapshot = db.collection("recipe")
            .whereEqualTo("teamId", teamId)
            .get()
            .await()

        val recipes = mutableListOf<Recipe>()

        for (document in recipeListSnapshot.documents) {
            // 기본 Recipe 필드 가져오기
            val recipe = document.toObject(Recipe::class.java) ?: continue

            // 하위 컬렉션 'ingredient' 가져오기
            val ingredientSnapshot = document.reference.collection("ingredient").get().await()
            val ingredients = ingredientSnapshot.toObjects(Ingredient::class.java)

            // Recipe에 하위 컬렉션 데이터를 추가
            recipes.add(
                recipe.copy(ingredients = ingredients) // 데이터 클래스 복사로 값 설정
            )
        }
        return recipes
    }

    /** Prep */

    override suspend fun getPrepCategory(teamId: String): MutableList<PrepCategory> {
        val prepCategory = db.collection("prepCategory").whereEqualTo("teamId", teamId).get().await()

        return if (prepCategory.isEmpty) mutableListOf()
        else prepCategory.toObjects(PrepCategory::class.java) as MutableList<PrepCategory>
    }

    override suspend fun getPrepList(categoryId: String): MutableList<Prep> {
        val prepList = db.collection("prep").whereEqualTo("categoryId", categoryId).get().await()

        return if (prepList.isEmpty) mutableListOf()
        else prepList.toObjects(Prep::class.java) as MutableList<Prep>
    }

    override suspend fun getAllMembers(teamId: String): MutableList<UserTeam> {
        val memberList = db.collection("user-team").whereEqualTo("teamId", teamId).get().await()

        return if(memberList.isEmpty) mutableListOf()
        else memberList.toObjects(UserTeam::class.java) as MutableList<UserTeam>
    }

    override suspend fun getRecipeName(recipeId: String): String {
        val recipeName = db.collection("recipe").whereEqualTo("id", recipeId).get().await().documents.firstOrNull()

        return recipeName?.getString("name") ?: ""
    }

    /** schedule time */

    override suspend fun getScheduleTimes(teamId: String): MutableList<ScheduleTime> {
        val scheduleTime = db.collection("scheduleTime").whereEqualTo("teamId", teamId).orderBy("startTime").get().await()

        return if (scheduleTime.isEmpty) mutableListOf()
        else scheduleTime.toObjects(ScheduleTime::class.java) as MutableList<ScheduleTime>
    }

    /** department / staff level management */
    override suspend fun getStaffLevels(departmentId: String): MutableList<StaffLevel> {
        val staffLevels = db.collection("staffLevel").whereEqualTo("departmentId", departmentId).get().await()

        return if (staffLevels.isEmpty) mutableListOf()
        else staffLevels.toObjects(StaffLevel::class.java)
    }

    override suspend fun getNotices(teamId: String): MutableList<Notice> {
        val notices = db.collection("notice").whereEqualTo("teamId", teamId).get().await()

        return if (notices.isEmpty) mutableListOf()
        else notices.toObjects(Notice::class.java)
    }
}