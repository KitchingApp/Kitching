package com.kitching.data.repository

import com.kitching.data.datasource.ScheduleTimeDataSourceImpl
import com.kitching.data.dto.ScheduleTimeChipsDTO
import com.kitching.data.dto.ScheduleTimeListDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.domain.datasource.ScheduleTimeDataSource
import com.kitching.domain.repository.ScheduleTimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ScheduleTimeRepositoryImpl(private val scheduleTimeDataSource: ScheduleTimeDataSource = ScheduleTimeDataSourceImpl()) :
    ScheduleTimeRepository {
    override fun getScheduleTimes(teamId: String): Flow<FirebaseResult<List<ScheduleTimeListDTO>>> =
        flow {
            emit(FirebaseResult.Loading)
            val scheduleTimes = scheduleTimeDataSource.getScheduleTimes(teamId)
            if (scheduleTimes.isEmpty()) emit(FirebaseResult.Success(emptyList()))
            else emit(FirebaseResult.Success(scheduleTimes.map {
                ScheduleTimeListDTO(
                    scheduleTimeId = it.id,
                    scheduleTimeName = it.name,
                    color = it.color,
                    startTime = it.startTime,
                    endTime = it.endTime
                )
            }))
        }.catch {
            emit(FirebaseResult.Failure(it))
        }

    override fun getScheduleTimesForChips(teamId: String): Flow<FirebaseResult<List<ScheduleTimeChipsDTO>>> =
        flow {
            emit(FirebaseResult.Loading)
            val scheduleTimes = scheduleTimeDataSource.getScheduleTimes(teamId)
            if (scheduleTimes.isEmpty()) emit(FirebaseResult.Success(emptyList()))
            else emit(FirebaseResult.Success(scheduleTimes.map {
                ScheduleTimeChipsDTO(
                    scheduleTimeId = it.id,
                    scheduleTimeName = it.name
                )
            }))
        }.catch {
            emit(FirebaseResult.Failure(it))
        }

    override fun createScheduleTime(
        teamId: String,
        name: String,
        color: String,
        startTime: String,
        endTime: String
    ): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result =
            scheduleTimeDataSource.createScheduleTime(teamId, name, color, startTime, endTime)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun updateScheduleTime(
        scheduleTimeId: String,
        name: String,
        color: String,
        startTime: String,
        endTime: String
    ): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = scheduleTimeDataSource.updateScheduleTime(
            scheduleTimeId,
            name,
            color,
            startTime,
            endTime
        )
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun deleteScheduleTime(scheduleTimeId: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = scheduleTimeDataSource.deleteScheduleTime(scheduleTimeId)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

}