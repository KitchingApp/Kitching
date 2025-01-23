package com.kitching.data.repository

import com.kitching.data.datasource.ScheduleDataSourceImpl
import com.kitching.data.datasource.ScheduleInfoDataSourceImpl
import com.kitching.data.dto.ScheduleDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.domain.datasource.ScheduleDataSource
import com.kitching.domain.datasource.ScheduleInfoDataSource
import com.kitching.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ScheduleRepositoryImpl(
    private val scheduleInfoDataSource: ScheduleInfoDataSource = ScheduleInfoDataSourceImpl(),
    private val scheduleDataSource: ScheduleDataSource = ScheduleDataSourceImpl()
) : ScheduleRepository {
    override fun getSchedules(
        teamId: String,
        date: String
    ): Flow<FirebaseResult<List<ScheduleDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val scheduleInfos = scheduleInfoDataSource.getScheduleInfos(teamId, date)
        if (scheduleInfos.isEmpty()) emit(FirebaseResult.Success(emptyList()))
        else emit(FirebaseResult.Success(scheduleInfos.map {
            ScheduleDTO(
                scheduleId = it.schedule.id,
                date = it.schedule.date,
                departmentName = it.department?.name,
                userId = it.user.id,
                userName = it.user.userName,
                scheduleTimeName = it.scheduleTime.name,
                isFix = it.schedule.isFix
            )
        }))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun createSchedule(
        teamId: String,
        dateString: String,
        userId: String,
        scheduleTimeId: String,
        isFix: Boolean
    ): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result =
            scheduleDataSource.createSchedule(teamId, dateString, userId, scheduleTimeId, isFix)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun deleteSchedule(scheduleId: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = scheduleDataSource.deleteSchedule(scheduleId)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun applySchedule(scheduleId: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = scheduleDataSource.applySchedule(scheduleId)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }
}