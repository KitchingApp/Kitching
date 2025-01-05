package com.kitching.data.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
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

class FireStoreDataSource(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    final val COLLECTION_DEPARTMENT = "department"
    final val COLLECTION_NOTICE = "notice"
    final val COLLECTION_ORDER = "order"
    final val COLLECTION_ORDER_CATEGORY = "orderCategory"
    final val COLLECTION_PREP = "prep"
    final val COLLECTION_PREP_CATEGORY = "prepCategory"
    final val COLLECTION_RECIPE = "recipe"
    final val COLLECTION_SCHEDULE = "schedule"
    final val COLLECTION_SCHEDULE_TIME = "scheduleTime"
    final val COLLECTION_STAFF_LEVEL = "staffLevel"
    final val COLLECTION_TEAM = "team"
    final val COLLECTION_UESR = "user"
    final val COLLECTION_USER_TEAM = "user-team"

    suspend fun getTeams(userId: String): List<Team> {
        val teams = db.collection(COLLECTION_TEAM).whereEqualTo("ownerId", userId).get().await()

        return if(teams.isEmpty) emptyList()
        else teams.toObjects(Team::class.java)
    }

    suspend fun getTeamName(teamId: String): String {
        val teamName = db.collection(COLLECTION_TEAM).whereEqualTo("id", teamId).get().await().documents.firstOrNull()

        return teamName?.getString("teamName")!!
    }

    suspend fun getDepartments(teamId: String): List<Department> {
        val departments = db.collection(COLLECTION_DEPARTMENT).whereEqualTo("teamId", teamId).get().await()

        return if(departments.isEmpty) emptyList()
        else departments.toObjects(Department::class.java)
    }

    suspend fun getTeamSchedules(teamId: String, date: String): List<Schedule> {
        val schedules = db.collection(COLLECTION_SCHEDULE).whereEqualTo("teamId", teamId).whereEqualTo("date", date).get().await()

        return if(schedules.isEmpty) emptyList()
        else schedules.toObjects(Schedule::class.java)
    }

    suspend fun getScheduleTimeName(teamId: String, scheduleTimeId: String): String {
        val scheduleTime = db.collection(COLLECTION_SCHEDULE_TIME).whereEqualTo("id", scheduleTimeId).get().await().documents.firstOrNull()

        return scheduleTime?.getString("name")!!
    }

    suspend fun getDepartmentId(teamId: String, userId: String): String {
        val userTeam = db.collection(COLLECTION_USER_TEAM).whereEqualTo("teamId", teamId).whereEqualTo("userId", userId).get().await().documents.firstOrNull()

        return userTeam?.getString("departmentId")!!
    }

    suspend fun getDepartmentName(teamId: String, departmentId: String): String {
        val department = db.collection(COLLECTION_DEPARTMENT).whereEqualTo("id", departmentId).get().await().documents.firstOrNull()

        return department?.getString("name")!!
    }

    suspend fun getStaffLevelId(teamId: String, userId: String): String {
        val userTeam = db.collection(COLLECTION_USER_TEAM).whereEqualTo("teamId", teamId).whereEqualTo("userId", userId).get().await().documents.firstOrNull()

        return userTeam?.getString("staffLevelId")!!
    }

    suspend fun getStaffLevelName(staffLevelId: String): String {
        val department = db.collection(COLLECTION_STAFF_LEVEL).whereEqualTo("id", staffLevelId).get().await().documents.firstOrNull()

        return department?.getString("name")!!
    }

    suspend fun getUserName(userId: String): String {
        val user = db.collection(COLLECTION_UESR).whereEqualTo("id", userId).get().await().documents.firstOrNull()

        return user?.getString("userName")!!
    }

    suspend fun createSchedule(teamId: String, dateString: String, userId: String, scheduleTimeId: String, isFix: Boolean): Boolean {
        var createTaskResult = false

        val scheduleWithOutId = Schedule(
            id = "",
            date = dateString,
            scheduleTimeId = scheduleTimeId,
            teamId = teamId,
            userId = userId,
            isFix = isFix
        )

        val document = db.collection(COLLECTION_SCHEDULE).add(scheduleWithOutId).await()

        val scheduleWithId = scheduleWithOutId.copy(id = document.id)
        db.collection(COLLECTION_SCHEDULE).document(document.id).set(scheduleWithId).addOnSuccessListener {
            createTaskResult = true
        }.await()

        return createTaskResult
    }

    suspend fun deleteSchedule(scheduleId: String): Boolean {
        var deleteTaskResult = false

        db.collection(COLLECTION_SCHEDULE).document(scheduleId).delete()
            .addOnSuccessListener { deleteTaskResult = true }

        return deleteTaskResult
    }

    /** Order Page */

    suspend fun getOrderCategory(teamId: String): MutableList<OrderCategory> {
        val orderCategory = db.collection(COLLECTION_ORDER_CATEGORY).whereEqualTo("teamId", teamId).get().await()
        return if (orderCategory.isEmpty) mutableListOf()
        else orderCategory.toObjects(OrderCategory::class.java) as MutableList<OrderCategory>
    }

    suspend fun getOrderList(categoryId: String): MutableList<Order> {
        val orderList = db.collection(COLLECTION_ORDER).whereEqualTo("categoryId", categoryId).get().await()
        return if (orderList.isEmpty) mutableListOf()
        else orderList.toObjects(Order::class.java) as MutableList<Order>

    }

    /** Recipe Page */
    suspend fun getRecipeList(teamId: String): MutableList<Recipe> {
        val recipeListSnapshot = db.collection(COLLECTION_RECIPE)
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

    suspend fun getPrepCategory(teamId: String): MutableList<PrepCategory> {
        val prepCategory = db.collection(COLLECTION_PREP_CATEGORY).whereEqualTo("teamId", teamId).get().await()

        return if (prepCategory.isEmpty) mutableListOf()
        else prepCategory.toObjects(PrepCategory::class.java) as MutableList<PrepCategory>
    }

    suspend fun getPrepList(categoryId: String): MutableList<Prep> {
        val prepList = db.collection(COLLECTION_PREP).whereEqualTo("categoryId", categoryId).get().await()

        return if (prepList.isEmpty) mutableListOf()
        else prepList.toObjects(Prep::class.java) as MutableList<Prep>
    }

    suspend fun getAllMembers(teamId: String): MutableList<UserTeam> {
        val memberList = db.collection(COLLECTION_USER_TEAM).whereEqualTo("teamId", teamId).get().await()

        return if(memberList.isEmpty) mutableListOf()
        else memberList.toObjects(UserTeam::class.java) as MutableList<UserTeam>
    }

    suspend fun getRecipeName(recipeId: String): String {
        val recipeName = db.collection(COLLECTION_RECIPE).whereEqualTo("id", recipeId).get().await().documents.firstOrNull()

        return recipeName?.getString("name") ?: ""
    }

    /** schedule time */

    suspend fun getScheduleTimes(teamId: String): MutableList<ScheduleTime> {
        val scheduleTime = db.collection(COLLECTION_SCHEDULE_TIME).whereEqualTo("teamId", teamId).orderBy("startTime").get().await()

        return if (scheduleTime.isEmpty) mutableListOf()
        else scheduleTime.toObjects(ScheduleTime::class.java) as MutableList<ScheduleTime>
    }

    /** department / staff level management */
    suspend fun getStaffLevels(departmentId: String): MutableList<StaffLevel> {
        val staffLevels = db.collection(COLLECTION_STAFF_LEVEL).whereEqualTo("departmentId", departmentId).get().await()

        return if (staffLevels.isEmpty) mutableListOf()
        else staffLevels.toObjects(StaffLevel::class.java)
    }

    suspend fun getNotices(teamId: String): MutableList<Notice> {
        val notices = db.collection(COLLECTION_NOTICE).whereEqualTo("teamId", teamId).get().await()

        return if (notices.isEmpty) mutableListOf()
        else notices.toObjects(Notice::class.java)
    }
}