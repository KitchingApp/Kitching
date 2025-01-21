package com.kitching.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.kitching.common.COLLECTION_SCHEDULE_TIME
import com.kitching.domain.datasource.ScheduleTimeDataSource
import com.kitching.domain.entities.ScheduleTime
import kotlinx.coroutines.tasks.await

class ScheduleTimeDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()):
    ScheduleTimeDataSource {
    override suspend fun getScheduleTime(scheduleTimeId: String): Result<ScheduleTime> {
        return runCatching {
            db.collection(COLLECTION_SCHEDULE_TIME).whereEqualTo("id", scheduleTimeId).get().await().toObjects(ScheduleTime::class.java).first()
        }
    }

    override suspend fun getScheduleTimes(teamId: String): Result<List<ScheduleTime>> {
        return runCatching {
            db.collection(COLLECTION_SCHEDULE_TIME).whereEqualTo("teamId", teamId).get().await().toObjects(ScheduleTime::class.java)
        }
    }

    override suspend fun createScheduleTime(
        teamId: String,
        name: String,
        startTime: String,
        endTime: String,
        color: String
    ): Boolean {
        return runCatching {
            db.collection(COLLECTION_SCHEDULE_TIME).add(
                ScheduleTime(
                    id = "",
                    teamId = teamId,
                    name = name,
                    startTime = startTime
                )
            ).await().apply {
                update("id", id).await()
            }
        }.isSuccess
    }

    override suspend fun updateScheduleTime(
        scheduleTimeId: String,
        name: String,
        startTime: String,
        endTime: String,
        color: String
    ): Boolean {
        return runCatching {
            db.collection(COLLECTION_SCHEDULE_TIME).document(scheduleTimeId).update("name", name, "startTime", startTime, "endTime", endTime, "color", color).await()
        }.isSuccess
    }

    override suspend fun deleteScheduleTime(scheduleTimeId: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_SCHEDULE_TIME).document(scheduleTimeId).delete().await()
        }.isSuccess
    }
}