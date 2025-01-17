package com.kitching.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.kitching.common.COLLECTION_DEPARTMENT
import com.kitching.common.COLLECTION_SCHEDULE
import com.kitching.common.COLLECTION_SCHEDULE_TIME
import com.kitching.common.COLLECTION_USER
import com.kitching.common.COLLECTION_USER_TEAM
import com.kitching.domain.datasource.ScheduleDataSource
import com.kitching.domain.entities.Department
import com.kitching.domain.entities.Schedule
import com.kitching.domain.entities.ScheduleInfo
import com.kitching.domain.entities.ScheduleTime
import com.kitching.domain.entities.User
import com.kitching.domain.entities.UserTeam
import kotlinx.coroutines.tasks.await

class ScheduleDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) :
    ScheduleDataSource {
    override suspend fun getDepartments(teamId: String): Result<List<Department>> {
        return runCatching {
            val department = db.collection(COLLECTION_DEPARTMENT)
                .whereEqualTo("teamId", teamId)
                .get()
                .await()

            department.toObjects(Department::class.java)
        }
    }

    override suspend fun getScheduleInfos(teamId: String, date: String): Result<List<ScheduleInfo>> {
        return runCatching {
            db.collection(COLLECTION_SCHEDULE)
                .whereEqualTo("teamId", teamId)
                .whereEqualTo("date", date)
                .get()
                .await()
                .toObjects(Schedule::class.java)
                .map {
                    val userTeam =
                        db.collection(COLLECTION_USER_TEAM).whereEqualTo("userId", it.userId)
                            .whereEqualTo("teamId", it.teamId).get().await().first()
                            .toObject(UserTeam::class.java)
                    ScheduleInfo(
                        schedule = it,
                        user = db.collection(COLLECTION_USER).document(it.userId).get().await()
                            .toObject(User::class.java)!!,
                        department = db.collection(COLLECTION_DEPARTMENT)
                            .document(userTeam.departmentId ?: "").get().await()
                            .toObject(Department::class.java),
                        scheduleTime = db.collection(COLLECTION_SCHEDULE_TIME)
                            .document(it.scheduleTimeId).get().await()
                            .toObject(ScheduleTime::class.java)!!
                    )
                }
        }
    }

    override suspend fun getMembers(teamId: String): Result<List<User>> {
        return runCatching {
            val members = db.collection(COLLECTION_USER)
                .whereEqualTo("teamId", teamId)
                .get()
                .await()

            members.toObjects(User::class.java)
        }
    }

    override suspend fun getScheduleTimes(teamId: String): Result<List<ScheduleTime>> {
        return runCatching {
            val schedules =
                db.collection(COLLECTION_SCHEDULE).whereEqualTo("teamId", teamId).get().await()

            schedules.toObjects(ScheduleTime::class.java)
        }
    }

    override suspend fun createSchedule(
        teamId: String,
        dateString: String,
        userId: String,
        scheduleTimeId: String,
        isFix: Boolean
    ): Boolean {
        return runCatching {
            db.collection(COLLECTION_SCHEDULE).add(
                Schedule(
                    id = "",
                    date = dateString,
                    scheduleTimeId = scheduleTimeId,
                    teamId = teamId,
                )
            ).await().apply {
                this.update("id", this.id).await()
            }
        }.isSuccess
    }

    override suspend fun deleteSchedule(scheduleId: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_SCHEDULE).document(scheduleId).delete()
        }.isSuccess
    }

    override suspend fun applySchedule(scheduleId: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_SCHEDULE).document(scheduleId).update("isFix", true).await()
        }.isSuccess
    }
}