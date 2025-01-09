package com.kitching.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.kitching.common.COLLECTION_DEPARTMENT
import com.kitching.common.COLLECTION_NOTICE
import com.kitching.common.COLLECTION_ORDER
import com.kitching.common.COLLECTION_ORDER_CATEGORY
import com.kitching.common.COLLECTION_PREP
import com.kitching.common.COLLECTION_PREP_CATEGORY
import com.kitching.common.COLLECTION_RECIPE
import com.kitching.common.COLLECTION_SCHEDULE
import com.kitching.common.COLLECTION_SCHEDULE_TIME
import com.kitching.common.COLLECTION_STAFF_LEVEL
import com.kitching.common.COLLECTION_TEAM
import com.kitching.common.COLLECTION_USER
import com.kitching.common.COLLECTION_USER_TEAM
import com.kitching.common.util.dateFormatter
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
import java.time.LocalDate

class FireStoreDataSource(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    suspend fun checkAndSaveUser(uid: String, userName: String, userImage: String): Boolean {
        return try {
            val userRef = db.collection(COLLECTION_USER).document(uid)
            val userSnapshot = userRef.get().await()

            if (userSnapshot.exists()) {
                true
            } else {
                val userMap = mapOf(
                    "id" to uid,
                    "userName" to userName,
                    "userImage" to userImage
                )
                userRef.set(userMap).await()
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getTeams(userId: String): List<Team> {
        // 1. user-team 컬렉션에서 조건에 맞는 teamId들 가져오기
        val userTeams = db.collection(COLLECTION_USER_TEAM)
            .whereEqualTo("userId", userId)
            .whereEqualTo("isManager", true)
            .get()
            .await()

        // teamId 리스트 생성
        val teamIds = userTeams.documents.mapNotNull { it.getString("teamId") }

        if (teamIds.isEmpty()) return emptyList()

        // 2. team 컬렉션에서 teamId가 일치하는 팀들 가져오기
        val teamsQuery = db.collection(COLLECTION_TEAM)
            .whereIn("id", teamIds)
            .get()
            .await()

        // 3. 가져온 데이터를 Team 객체 리스트로 변환
        return if (teamsQuery.isEmpty) emptyList()
        else teamsQuery.toObjects(Team::class.java)
    }

    suspend fun createTeam(inviteCode: String, ownerId: String, teamName: String): Boolean {
        return try {
            val teamData = mapOf(
                "id" to "",
                "inviteCode" to inviteCode,
                "ownerId" to ownerId,
                "teamName" to teamName
            )
            val teamDocument = db.collection(COLLECTION_TEAM).add(teamData).await()

            db.collection(COLLECTION_TEAM).document(teamDocument.id).update("id", teamDocument.id).await()

            val userTeamData = mapOf(
                "id" to "",
                "isManager" to true,
                "teamId" to teamDocument.id,
                "userId" to ownerId,
                "departmentId" to "",
                "staffLevelId" to ""
            )
            val userTeamDocument = db.collection(COLLECTION_USER_TEAM).add(userTeamData).await()

            db.collection(COLLECTION_USER_TEAM).document(userTeamDocument.id).update("id", userTeamDocument.id).await()

            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getTeamName(teamId: String): String {
        val teamName = db.collection(COLLECTION_TEAM).whereEqualTo("id", teamId).get()
            .await().documents.firstOrNull()

        return teamName?.getString("teamName")!!
    }

    suspend fun getDepartments(teamId: String): List<Department> {
        val departments =
            db.collection(COLLECTION_DEPARTMENT).whereEqualTo("teamId", teamId).get().await()

        return if (departments.isEmpty) emptyList()
        else departments.toObjects(Department::class.java)
    }

    suspend fun getTeamSchedules(teamId: String, date: String): List<Schedule> {
        val schedules = db.collection(COLLECTION_SCHEDULE).whereEqualTo("teamId", teamId)
            .whereEqualTo("date", date).get().await()

        return if (schedules.isEmpty) emptyList()
        else schedules.toObjects(Schedule::class.java)
    }

    suspend fun getDepartmentId(teamId: String, userId: String): String {
        val userTeam = db.collection(COLLECTION_USER_TEAM).whereEqualTo("teamId", teamId)
            .whereEqualTo("userId", userId).get().await().documents.firstOrNull()

        return userTeam?.getString("departmentId")!!
    }

    suspend fun getDepartmentName(teamId: String, departmentId: String): String {
        val department = db.collection(COLLECTION_DEPARTMENT).whereEqualTo("id", departmentId).get()
            .await().documents.firstOrNull()

        return department?.getString("name")!!
    }

    suspend fun getStaffLevelId(teamId: String, userId: String): String {
        val userTeam = db.collection(COLLECTION_USER_TEAM).whereEqualTo("teamId", teamId)
            .whereEqualTo("userId", userId).get().await().documents.firstOrNull()

        return userTeam?.getString("staffLevelId")!!
    }

    suspend fun getStaffLevelName(staffLevelId: String): String {
        val department =
            db.collection(COLLECTION_STAFF_LEVEL).whereEqualTo("id", staffLevelId).get()
                .await().documents.firstOrNull()

        return department?.getString("name")!!
    }

    suspend fun getUserName(userId: String): String {
        val user = db.collection(COLLECTION_USER).whereEqualTo("id", userId).get()
            .await().documents.firstOrNull()

        return user?.getString("userName")!!
    }

    suspend fun getSchedule(scheduleId: String): Schedule? {
        val schedule = db.collection(COLLECTION_SCHEDULE).whereEqualTo("id", scheduleId).get()
            .await().documents.firstOrNull()

        return schedule?.toObject(Schedule::class.java)
    }

    suspend fun createSchedule(
        teamId: String,
        dateString: String,
        userId: String,
        scheduleTimeId: String,
        isFix: Boolean
    ): Boolean {
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

        db.collection(COLLECTION_SCHEDULE).document(document.id).update("id", document.id)
            .addOnSuccessListener {
                createTaskResult = true
            }.await()

        return createTaskResult
    }

    suspend fun applySchedule(scheduleId: String): Boolean {
        var applyTaskResult = false

        if (getSchedule(scheduleId) !== null) {
            db.collection(COLLECTION_SCHEDULE).document(scheduleId).update("isFix", true)
                .addOnSuccessListener {
                    applyTaskResult = true
                }.await()
        }

        return applyTaskResult
    }

    suspend fun deleteSchedule(scheduleId: String): Boolean {
        var deleteTaskResult = false

        db.collection(COLLECTION_SCHEDULE).document(scheduleId).delete().addOnSuccessListener {
            deleteTaskResult = true
        }.await()

        return deleteTaskResult
    }

    /** Order Page */

    suspend fun getOrderCategory(teamId: String): MutableList<OrderCategory> {
        val orderCategory =
            db.collection(COLLECTION_ORDER_CATEGORY).whereEqualTo("teamId", teamId).get().await()
        return if (orderCategory.isEmpty) mutableListOf()
        else orderCategory.toObjects(OrderCategory::class.java) as MutableList<OrderCategory>
    }

    suspend fun getOrderList(categoryId: String): MutableList<Order> {
        val orderList =
            db.collection(COLLECTION_ORDER).whereEqualTo("categoryId", categoryId).get().await()
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
        val prepCategory =
            db.collection(COLLECTION_PREP_CATEGORY).whereEqualTo("teamId", teamId).get().await()

        return if (prepCategory.isEmpty) mutableListOf()
        else prepCategory.toObjects(PrepCategory::class.java) as MutableList<PrepCategory>
    }

    suspend fun createPrepCategory(teamId: String, categoryName: String, color: String): Boolean {
        var createTaskResult = false

        val prepCategoryWithOutId = PrepCategory(
            id = "",
            teamId = teamId,
            name = categoryName,
            color = color
        )

        val document = db.collection(COLLECTION_PREP_CATEGORY).add(prepCategoryWithOutId).await()

        db.collection(COLLECTION_PREP_CATEGORY).document(document.id).update("id", document.id)
            .addOnSuccessListener {
                createTaskResult = true
            }.await()

        return createTaskResult
    }

    suspend fun updatePrepCategory(
        categoryId: String,
        categoryName: String,
        color: String
    ): Boolean {
        var updateTaskResult = false

        val document = db.collection(COLLECTION_PREP_CATEGORY).document(categoryId)
            .update("name", categoryName, "color", color).addOnSuccessListener {
                updateTaskResult = true
            }.await()

        return updateTaskResult
    }

    suspend fun deletePrepCategory(scheduleId: String): Boolean {
        var deleteTaskResult = false

        val document = db.collection(COLLECTION_PREP_CATEGORY).document(scheduleId).delete()
            .addOnSuccessListener {
                deleteTaskResult = true
            }.await()

        return deleteTaskResult
    }

    suspend fun getPrepList(categoryId: String): MutableList<Prep> {
        val prepList =
            db.collection(COLLECTION_PREP).whereEqualTo("categoryId", categoryId).get().await()

        return if (prepList.isEmpty) mutableListOf()
        else prepList.toObjects(Prep::class.java) as MutableList<Prep>
    }

    suspend fun createPrep(categoryId: String, name: String): Boolean {
        var createTaskResult = false

        val prepWithOutId = Prep(
            categoryId = categoryId,
            id = "",
            name = name,
            recipeId = ""
        )

        runCatching {
            val document = db.collection(COLLECTION_PREP).add(prepWithOutId).await()

            db.collection(COLLECTION_PREP).document(document.id).update("id", document.id).await()

            createTaskResult = true
        }

        return createTaskResult
    }

    suspend fun updatePrep(prepId: String, name: String): Boolean {
        var updateTaskResult = false

        db.collection(COLLECTION_PREP).document(prepId).update("name", name).addOnSuccessListener {
            updateTaskResult = true
        }.await()

        return updateTaskResult
    }

    suspend fun deletePrep(prepId: String): Boolean {
        var deleteTaskResult = false

        db.collection(COLLECTION_PREP).document(prepId).delete().addOnSuccessListener {
            deleteTaskResult = true
        }.await()

        return deleteTaskResult
    }

    suspend fun getAllMembers(teamId: String): MutableList<UserTeam> {
        val memberList =
            db.collection(COLLECTION_USER_TEAM).whereEqualTo("teamId", teamId).get().await()

        return if (memberList.isEmpty) mutableListOf()
        else memberList.toObjects(UserTeam::class.java) as MutableList<UserTeam>
    }

    suspend fun getRecipeName(recipeId: String): String {
        val recipeName = db.collection(COLLECTION_RECIPE).whereEqualTo("id", recipeId).get()
            .await().documents.firstOrNull()

        return recipeName?.getString("name") ?: ""
    }

    suspend fun getNotices(teamId: String): MutableList<Notice> {
        val notices = db.collection(COLLECTION_NOTICE).whereEqualTo("teamId", teamId).get().await()

        return if (notices.isEmpty) mutableListOf()
        else notices.toObjects(Notice::class.java)
    }

    suspend fun createNotice(userId: String, teamId: String, title: String, content: String): Boolean {
        var createTaskResult = false

        val noticeWithOutId = Notice(
            id = "",
            date = LocalDate.now().toString(),
            title = title,
            content = content,
            writerId = userId,
            teamId = teamId
        )

        db.collection(COLLECTION_NOTICE).add(noticeWithOutId).await().apply {
            this.update("id", this.id).addOnSuccessListener { createTaskResult = true }.await()
        }

        return createTaskResult
    }

    suspend fun updateNotice(noticeId: String, title: String, content: String): Boolean {
        var updateTaskResult = false

        db.collection(COLLECTION_NOTICE).document(noticeId).update("title", title, "content", content).addOnSuccessListener {
            updateTaskResult = true
        }.await()

        return updateTaskResult
    }

    suspend fun deleteNotice(noticeId: String): Boolean {
        var deleteTaskResult = false

        db.collection(COLLECTION_NOTICE).document(noticeId).delete().addOnSuccessListener {
            deleteTaskResult = true
        }.await()

        return deleteTaskResult
    }

    /** department / staff level management */

    suspend fun createDepartment(teamId: String, name: String, color: String): Boolean {
        var createTaskResult = false

        val departmentWithOutId = Department(
            id = "",
            teamId = teamId,
            name = name,
            color = color
        )

        db.collection(COLLECTION_DEPARTMENT).add(departmentWithOutId).await().apply {
            this.update("id", this.id).addOnSuccessListener { createTaskResult = true }.await()
        }

        return createTaskResult
    }

    suspend fun updateDepartment(departmentId: String, name: String, color: String): Boolean {
        var updateTaskResult = false

        db.collection(COLLECTION_DEPARTMENT).document(departmentId).update("name", name, "color", color).addOnSuccessListener {
            updateTaskResult = true
        }.await()

        return updateTaskResult
    }

    suspend fun deleteDepartment(departmentId: String): Boolean {
        var deleteTaskResult = false

        db.collection(COLLECTION_DEPARTMENT).document(departmentId).delete().addOnSuccessListener {
            deleteTaskResult = true
        }.await()

        return deleteTaskResult
    }

    suspend fun getStaffLevels(departmentId: String): MutableList<StaffLevel> {
        val staffLevels =
            db.collection(COLLECTION_STAFF_LEVEL).whereEqualTo("departmentId", departmentId).get()
                .await()

        return if (staffLevels.isEmpty) mutableListOf()
        else staffLevels.toObjects(StaffLevel::class.java)
    }

    suspend fun createStaffLevel(departmentId: String, staffLevelName: String): Boolean {
        var createTaskResult = false

        val staffLevelWithOutId = StaffLevel(
            id = "",
            departmentId = departmentId,
            name = staffLevelName
        )

        db.collection(COLLECTION_STAFF_LEVEL).add(staffLevelWithOutId).await().apply {
            this.update("id", this.id).addOnSuccessListener {
                createTaskResult = true
            }.await()
        }

        return createTaskResult
    }

    suspend fun updateStaffLevel(staffLevelId: String, name: String): Boolean {
        var updateTaskResult = false

        db.collection(COLLECTION_STAFF_LEVEL).document(staffLevelId).update("name", name).addOnSuccessListener {
            updateTaskResult = true
        }.await()

        return updateTaskResult
    }

    suspend fun deleteStaffLevel(staffLevelId: String): Boolean {
        var deleteTaskResult = false

        db.collection(COLLECTION_STAFF_LEVEL).document(staffLevelId).delete().addOnSuccessListener {
            deleteTaskResult = true
        }.await()

        return deleteTaskResult
    }

    /** schedule time */

    suspend fun getScheduleTimeName(teamId: String, scheduleTimeId: String): String {
        val scheduleTime =
            db.collection(COLLECTION_SCHEDULE_TIME).whereEqualTo("id", scheduleTimeId).get()
                .await().documents.firstOrNull()

        return scheduleTime?.getString("name")!!
    }

    suspend fun getScheduleTimes(teamId: String): MutableList<ScheduleTime> {
        val scheduleTime = db.collection(COLLECTION_SCHEDULE_TIME).whereEqualTo("teamId", teamId).get().await()

        return if (scheduleTime.isEmpty) mutableListOf()
        else scheduleTime.toObjects(ScheduleTime::class.java) as MutableList<ScheduleTime>
    }

    suspend fun createScheduleTime(teamId: String, name: String, startTime: String, endTime: String, color: String): Boolean {
        var createTaskResult = false

        val scheduleTimeWithOutId = ScheduleTime(
            id = "",
            name = name,
            startTime = startTime,
            endTime = endTime,
            color = color,
            teamId = teamId
        )

        val document = db.collection(COLLECTION_SCHEDULE_TIME).add(scheduleTimeWithOutId).await().apply {
            update("id", this.id).addOnSuccessListener {
                createTaskResult = true
            }.await()
        }

        return createTaskResult
    }

    suspend fun updateScheduleTime(scheduleTimeId: String, name: String, startTime: String, endTime: String, color: String): Boolean {
        var updateTaskResult = false

        db.collection(COLLECTION_SCHEDULE_TIME).document(scheduleTimeId).update("name", name, "startTime", startTime, "endTime", endTime, "color", color).addOnSuccessListener {
            updateTaskResult = true
        }.await()

        return updateTaskResult
    }

    suspend fun deleteScheduleTime(scheduleTimeId: String): Boolean {
        var deleteTaskResult = false

        db.collection(COLLECTION_SCHEDULE_TIME).document(scheduleTimeId).delete().addOnSuccessListener {
            deleteTaskResult = true
        }.await()

        return deleteTaskResult
    }
}