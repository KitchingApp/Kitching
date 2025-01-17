package com.kitching.data.repository

import com.kitching.data.datasource.ScheduleDataSourceImpl
import com.kitching.data.dto.ScheduleDTO
import com.kitching.data.dto.DropDownDepartmentsDTO
import com.kitching.data.dto.DropDownMembersDTO
import com.kitching.data.dto.ScheduleTimeChipsDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.domain.datasource.ScheduleDataSource
import com.kitching.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ScheduleRepositoryImpl(private val dataSource: ScheduleDataSource = ScheduleDataSourceImpl()): ScheduleRepository {
    override fun getDepartments(teamId: String): Flow<FirebaseResult<List<DropDownDepartmentsDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val departments = dataSource.getDepartments(teamId).getOrThrow().map {
            DropDownDepartmentsDTO(
                departmentId = it.id,
                departmentName = it.name
            )
        }
        emit(FirebaseResult.Success(departments))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun getSchedules(
        teamId: String,
        date: String
    ): Flow<FirebaseResult<List<ScheduleDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val scheduleInfos = dataSource.getScheduleInfos(teamId, date).getOrThrow().map {
            ScheduleDTO(
                scheduleId = it.schedule.id,
                date = it.schedule.date,
                departmentName = it.department?.name,
                userId = it.user.id,
                userName = it.user.userName,
                scheduleTimeName = it.scheduleTime.name,
                isFix = it.schedule.isFix
            )
        }
        emit(FirebaseResult.Success(scheduleInfos))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun getMembers(teamId: String): Flow<FirebaseResult<List<DropDownMembersDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val members = dataSource.getMembers(teamId).getOrThrow().map {
            DropDownMembersDTO(
                userId = it.id,
                userName = it.userName
            )
        }
        emit(FirebaseResult.Success(members))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun getScheduleTimes(teamId: String): Flow<FirebaseResult<List<ScheduleTimeChipsDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val scheduleTimes = dataSource.getScheduleTimes(teamId).getOrThrow().map {
            ScheduleTimeChipsDTO(
                scheduleTimeId = it.id,
                scheduleTimeName = it.name
            )
        }
        emit(FirebaseResult.Success(scheduleTimes))
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
        val result = dataSource.createSchedule(teamId, dateString, userId, scheduleTimeId, isFix)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun deleteSchedule(scheduleId: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = dataSource.deleteSchedule(scheduleId)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun applySchedule(scheduleId: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = dataSource.applySchedule(scheduleId)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }
}