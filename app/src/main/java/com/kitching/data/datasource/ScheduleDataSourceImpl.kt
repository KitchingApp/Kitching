package com.kitching.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.kitching.common.COLLECTION_SCHEDULE
import com.kitching.domain.datasource.ScheduleDataSource
import com.kitching.domain.entities.Schedule
import kotlinx.coroutines.tasks.await

class ScheduleDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) :
    ScheduleDataSource {
    override suspend fun getSchedules(teamId: String, dateString: String): Result<List<Schedule>> {
        return runCatching {
            db.collection(COLLECTION_SCHEDULE)
                .whereEqualTo("teamId", teamId)
                .whereEqualTo("date", dateString)
                .get()
                .await()
                .toObjects(Schedule::class.java)
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