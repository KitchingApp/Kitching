package com.kitching.domain.datasource

import com.kitching.domain.entities.ScheduleInfo

interface ScheduleInfoDataSource {
    suspend fun getScheduleInfos(teamId: String, date: String): Result<List<ScheduleInfo>>
}

